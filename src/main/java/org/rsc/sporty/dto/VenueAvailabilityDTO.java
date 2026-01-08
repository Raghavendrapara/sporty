package org.rsc.sporty.dto;

import org.rsc.sporty.entity.Venue;
import org.rsc.sporty.entity.VenueSlot;

import java.util.List;

public record VenueAvailabilityDTO(Venue venue, List<VenueSlot> availableSlots) {}