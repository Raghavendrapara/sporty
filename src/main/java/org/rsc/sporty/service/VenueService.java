package org.rsc.sporty.service;

import lombok.RequiredArgsConstructor;
import org.rsc.sporty.entity.Venue;
import org.rsc.sporty.entity.VenueSlot;
import org.rsc.sporty.repository.VenueRepository;
import org.rsc.sporty.repository.VenueSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final VenueSlotRepository venueSlotRepository;

    public Venue addVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
    }

    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }

    @Transactional
    public VenueSlot addSlot(Long venueId, LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.equals(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        Venue venue = getVenueById(venueId);

        List<VenueSlot> overlaps = venueSlotRepository.findOverlappingSlots(venueId, start, end);
        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("Slot overlaps with existing slots");
        }

        VenueSlot slot = new VenueSlot();
        slot.setVenue(venue);
        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setIsBooked(false);

        return venueSlotRepository.save(slot);
    }

    public List<Venue> findAvailableVenues(LocalDateTime start, LocalDateTime end) {
        List<VenueSlot> availableSlots = venueSlotRepository.findAvailableSlots(start, end);
        
        return availableSlots.stream()
                .map(VenueSlot::getVenue)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<VenueSlot> findAvailableSlots(LocalDateTime start, LocalDateTime end) {
        return venueSlotRepository.findAvailableSlots(start, end);
    }
}
