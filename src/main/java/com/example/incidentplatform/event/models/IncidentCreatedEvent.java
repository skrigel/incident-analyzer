package com.example.incidentplatform.event.models;

import com.example.incidentplatform.domain.Severity;

import java.time.Instant;
import java.util.UUID;

public record IncidentCreatedEvent(UUID incidentId,
                                   Severity severity,
                                   Instant createdAt,
                                   String source) {
}
