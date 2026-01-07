package org.rsc.sporty.api;

import lombok.RequiredArgsConstructor;
import org.rsc.sporty.dto.BookingRequest;
import org.rsc.sporty.dto.BookingResponse;
import org.rsc.sporty.entity.Booking;
import org.rsc.sporty.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingsController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(
                request.userId(),
                request.venueSlotId(),
                request.sportId()
        );
        BookingResponse response = new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getVenueSlot().getId(),
                booking.getSport().getId(),
                booking.getStatus().name(),
                booking.getBookingDate()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
