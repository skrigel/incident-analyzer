package com.example.incidentplatform.dto;

import com.example.incidentplatform.domain.Alert;
import com.example.incidentplatform.domain.Incident;
import com.example.incidentplatform.domain.IncidentStatus;
import com.example.incidentplatform.domain.Severity;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record IncidentResponse(
        UUID id,
        String title,
        Severity severity,
        String description,
        IncidentStatus status,
        String summary,
        Set<String> tags,
        List<AlertResponse> alerts,
        Instant createdAt,
        Instant resolvedAt
) {

    // Default factory — no alerts loaded
    public static IncidentResponse from(Incident i) {

        Set<String> defaultSet = Set.of();
        return new IncidentResponse(
                i.getId(),
                i.getTitle(),
                i.getSeverity(),
                i.getDescription(),
                i.getStatus(),
                i.getSummary(),
//                i.getAssignedTo() != null
//                        ? UserSummary.from(i.getAssignedTo()) : null,
                i.getTags() != null
                        ? Set.copyOf(i.getTags()) : defaultSet,
                null,
                i.getCreatedAt(),
                i.getResolvedAt()
        );
    }

    public static IncidentResponse withAlerts(
            Incident i, List<Alert> alerts) {
        Set<String> defaultSet = Set.of();
        return new IncidentResponse(
                i.getId(),
                i.getTitle(),
                i.getSeverity(),
                i.getDescription(),
                i.getStatus(),
                i.getSummary(),
                i.getTags() != null
                        ? Set.copyOf(i.getTags()) : defaultSet,
                alerts.stream()
                        .map(AlertResponse::from)
                        .toList(),
                i.getCreatedAt(),
                i.getResolvedAt()
        );
    }
}
