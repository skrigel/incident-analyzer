package com.example.incidentplatform.service;

import com.example.incidentplatform.domain.Alert;
import com.example.incidentplatform.domain.Incident;
import com.example.incidentplatform.domain.IncidentStatus;
import com.example.incidentplatform.domain.User;
import com.example.incidentplatform.dto.*;
import com.example.incidentplatform.event.IncidentEventPublisher;
import com.example.incidentplatform.event.models.IncidentCreatedEvent;
import com.example.incidentplatform.exception.NotFoundException;
import com.example.incidentplatform.repository.AlertRepository;
import com.example.incidentplatform.repository.IncidentRepository;
import com.example.incidentplatform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.example.incidentplatform.domain.IncidentStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {

    private final IncidentRepository incidentRepo;
    private final AlertRepository alertRepo;
    private final UserRepository userRepo;

//    public IncidentService(IncidentRepository incidentRepo, AlertRepository alertRepo, UserRepository userRepo) {
//        this.incidentRepo = incidentRepo;
//        this.alertRepo = alertRepo;
//        this.userRepo = userRepo;
//    }
    private final IncidentEventPublisher publisher;   // added Stage 3

    @Transactional
    @CacheEvict(value = "incident-lists", allEntries = true)
    public IncidentResponse create(CreateIncidentRequest req) {
        Incident incident = new Incident();
        incident.setTitle(req.title());
        incident.setSeverity(req.severity());
        incident.setStatus(IncidentStatus.OPEN);

        Incident saved = incidentRepo.save(incident);

        // Publish Spring event (forwarded to Kafka)
        publisher.onIncidentCreated(
                new IncidentCreatedEvent(
                        saved.getId(),
                        saved.getSeverity(),
                        saved.getCreatedAt(),
                        "api"
                )
        );

        return IncidentResponse.from(saved);
    }

    // Only cache when alerts are NOT included (they change too frequently)
    @Transactional
    @ReadOnlyProperty
    @Cacheable(value = "incidents", key = "#id", condition = "!#includeAlerts")
    public IncidentResponse getById(UUID id, boolean includeAlerts) {
        Incident incident = findOrThrow(id);
        if (includeAlerts) {
            List<Alert> alerts = alertRepo.findByIncidentId(id);
            if (!alerts.isEmpty()){
                return IncidentResponse.withAlerts(incident, alerts);
            }
        }
        return IncidentResponse.from(incident);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "incidents", key = "#id"),
            @CacheEvict(value = "incident-lists", allEntries = true)
    })
    public IncidentResponse updateStatus(UUID id, UpdateStatusRequest req) {
        Incident incident = findOrThrow(id);
//        validateTransition(incident.getStatus(), req.status());

        incident.setStatus(req.status());
        if (req.status() == IncidentStatus.RESOLVED) {
            incident.setResolvedAt(Instant.now());
        }
        return IncidentResponse.from(incidentRepo.save(incident));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "incidents", key = "#id"),
            @CacheEvict(value = "incident-lists", allEntries = true)
    })
    public IncidentResponse assign(UUID id, AssignRequest req) {
        Incident incident = findOrThrow(id);
        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User", req.userId()));
        incident.setAssignedTo(user);
        return IncidentResponse.from(incidentRepo.save(incident));
    }

    @Transactional
    public List<AlertResponse> getAlerts(UUID id) {
        findOrThrow(id);  // validates incident exists first
        return alertRepo.findByIncidentId(id).stream()
                .map(AlertResponse::from).toList();
    }

    private Incident findOrThrow(UUID id) {
        return incidentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident", id));
    }

    private static final Map<IncidentStatus, Set<IncidentStatus>> TRANSITIONS = Map.of(
            OPEN,          Set.of(INVESTIGATING, RESOLVED),
            INVESTIGATING, Set.of(RESOLVED),
            RESOLVED,      Set.of()
    );

//    private void validateTransition(IncidentStatus from, IncidentStatus to) {
//        if (!TRANSITIONS.get(from).contains(to)) {
//            throw new InvalidTransitionException(from, to);
//        }
//    }
}