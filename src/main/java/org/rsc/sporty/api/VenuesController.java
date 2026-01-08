package org.rsc.sporty.api;

import lombok.RequiredArgsConstructor;
import org.rsc.sporty.dto.SlotRequest;
import org.rsc.sporty.dto.VenueAvailabilityDTO;
import org.rsc.sporty.dto.VenueRequest;
import org.rsc.sporty.entity.Venue;
import org.rsc.sporty.entity.VenueSlot;
import org.rsc.sporty.service.VenueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenuesController {

    private final VenueService venueService;

    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody VenueRequest request) {
        Venue venue = new Venue();
        venue.setName(request.name());
        venue.setLocation(request.location());
        return new ResponseEntity<>(venueService.addVenue(venue), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{venueId}/slots")
    public ResponseEntity<List<VenueSlot>> getSlotsForVenue(@PathVariable Long venueId) {
        return ResponseEntity.ok(venueService.getAllSlotsForVenue(venueId));
    }

    @PostMapping("/{venueId}/slots")
    public ResponseEntity<VenueSlot> addSlot(@PathVariable Long venueId, @RequestBody SlotRequest request) {
        return new ResponseEntity<>(
                venueService.addSlot(venueId, request.startTime(), request.endTime()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<VenueAvailabilityDTO>> getAvailableVenues(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(value = "sport_id", required = false) Integer sportId
    ) {
        List<VenueSlot> slots = venueService.findAvailableSlots(start, end);

        Map<Venue, List<VenueSlot>> grouped = slots.stream()
                .collect(Collectors.groupingBy(VenueSlot::getVenue));

        List<VenueAvailabilityDTO> response = grouped.entrySet().stream()
                .map(entry -> new VenueAvailabilityDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenues(@RequestParam(required = false) String location) {
        if (location != null && !location.isBlank()) {
            return ResponseEntity.ok(venueService.getVenuesByLocation(location));
        }
        return ResponseEntity.ok(venueService.getAllVenues());
    }


    

}
