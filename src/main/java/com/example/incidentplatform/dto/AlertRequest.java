package com.example.incidentplatform.dto;

import com.example.incidentplatform.domain.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AlertRequest(
        @NotBlank  UUID id,
        @NotBlank String source,
        @NotBlank String message,
        @NotNull Severity severity,
        Instant firedAt,
        Map<String, String> metadata
        ) {
}
