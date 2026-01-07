package org.rsc.sporty.dto;

import org.rsc.sporty.entity.Sport;

import java.util.List;

public record ApiResponse(
        String status,
        List<SportItem> data
) {}
