package org.rsc.sporty.dto;

import java.util.List;

public record ApiResponse(
        String status,
        List<SportItem> data
) {}
