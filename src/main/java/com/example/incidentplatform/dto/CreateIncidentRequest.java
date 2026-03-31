package com.example.incidentplatform.dto;

import com.example.incidentplatform.domain.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateIncidentRequest(
        @NotBlank String title,
        @NotNull Severity severity,
        Set<String> tags,
        String description
        ) {
}
