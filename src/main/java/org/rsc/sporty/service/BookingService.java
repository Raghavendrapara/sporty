package org.rsc.sporty.service;

import lombok.RequiredArgsConstructor;
import org.rsc.sporty.entity.Booking;
import org.rsc.sporty.entity.Sport;
import org.rsc.sporty.entity.User;
import org.rsc.sporty.entity.VenueSlot;
import org.rsc.sporty.repository.BookingRepository;
import org.rsc.sporty.repository.SportRepository;
import org.rsc.sporty.repository.UserRepository;
import org.rsc.sporty.repository.VenueSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VenueSlotRepository venueSlotRepository;
    private final UserRepository userRepository;
    private final SportRepository sportRepository;

    @Transactional
    public Booking createBooking(UUID userId, Long slotId, Integer sportId) {
        VenueSlot slot = venueSlotRepository.findByIdWithLock(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getIsBooked()) {
            throw new IllegalStateException("Slot is already booked");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new RuntimeException("Sport not found"));

        slot.setIsBooked(true);
        venueSlotRepository.save(slot);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVenueSlot(slot);
        booking.setSport(sport);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            return;
        }

        VenueSlot slot = booking.getVenueSlot();
        slot.setIsBooked(false);
        venueSlotRepository.save(slot);

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}