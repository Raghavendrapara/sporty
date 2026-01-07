package org.rsc.sporty.dto;

import java.time.LocalDateTime;

public record SlotRequest(LocalDateTime startTime, LocalDateTime endTime) {}
