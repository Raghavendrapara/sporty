package org.rsc.sporty.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_slot_id", nullable = false, unique = true)
    private VenueSlot venueSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Column(name = "booking_date", updatable = false)
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('CONFIRMED', 'CANCELLED', 'COMPLETED')")
    private BookingStatus status = BookingStatus.CONFIRMED;

    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
    }

    public enum BookingStatus {
        CONFIRMED, CANCELLED, COMPLETED
    }
}
