package org.rsc.sporty.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SportItem(
        @JsonProperty("sport_id") String externalId,
        @JsonProperty("sport_name") String name
) {}