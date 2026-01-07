package org.rsc.sporty.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID userId,
        Long venueSlotId,
        Integer sportId,
        String status,
        LocalDateTime bookingDate
) {}