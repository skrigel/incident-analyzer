package com.example.incidentplatform.dto;

import com.example.incidentplatform.domain.IncidentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateStatusRequest(
        @NotNull IncidentStatus status,
        @Size(max=500) String reason
        ) {
}
