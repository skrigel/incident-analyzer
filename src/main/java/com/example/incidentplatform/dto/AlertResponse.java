package com.example.incidentplatform.dto;

import com.example.incidentplatform.domain.Alert;
import com.example.incidentplatform.domain.Incident;
import com.example.incidentplatform.domain.Severity;

import java.time.Instant;
import java.util.UUID;

public record AlertResponse(
        UUID id,
        Incident incident,
        String source,
        String message,
        Severity severity,
        Instant firedAt
) {

    public static AlertResponse from(Alert a) {
        return new AlertResponse(
                a.getId(),
                a.getIncident(),
                a.getSource(),
                a.getMessage(),
                a.getSeverity(),
                a.getFiredAt()
        );
    }
}
