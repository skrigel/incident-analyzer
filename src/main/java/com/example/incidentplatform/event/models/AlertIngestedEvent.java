package com.example.incidentplatform.event.models;

import java.time.Instant;
import java.util.UUID;

public record AlertIngestedEvent(UUID requestId,
                                 Instant createdAt) {
}
