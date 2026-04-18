package com.example.incidentplatform.event.models;

import com.example.incidentplatform.domain.IncidentStatus;
import com.example.incidentplatform.domain.Severity;

import java.time.Instant;
import java.util.UUID;

public record IncidentStatusChangeEvent(UUID incidentId,
                                        Severity severity,
                                        IncidentStatus prevStatus,
                                        IncidentStatus newStatus,
                                        Instant changedAt) {
}
