package com.example.incidentplatform.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;

public record CreateSourceRequest(
    @NotBlank String name,
    @NotBlank String sourceType,
    JsonNode transformConfig
) {
}
