package org.rsc.sporty.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record BookingRequest(
    @JsonProperty("user_id") UUID userId,
    @JsonProperty("venue_slot_id") Long venueSlotId,
    @JsonProperty("sport_id") Integer sportId
) {}