package com.example.incidentplatform.service;

import com.example.incidentplatform.domain.Alert;
import com.example.incidentplatform.domain.Incident;
import com.example.incidentplatform.domain.IncidentStatus;
import com.example.incidentplatform.dto.AlertRequest;
import com.example.incidentplatform.dto.AlertResponse;
import com.example.incidentplatform.repository.AlertRepository;
import com.example.incidentplatform.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepo;
    private final IncidentRepository incidentRepo;
//    private final IncidentEventPublisher publisher;  // Stage 3

    @Transactional
    public AlertResponse ingest(AlertRequest req) {
        Incident incident = incidentRepo
                .findFirstBySourceAndStatus(req.source(), IncidentStatus.OPEN)
                .orElseGet(() -> createIncidentForAlert(req));

        Alert alert = new Alert();
        alert.setIncident(incident);
        alert.setSource(req.source());
        alert.setMessage(req.message());
        alert.setSeverity(req.severity());
        alert.setFiredAt(req.firedAt() != null ? req.firedAt() : Instant.now());

        Alert saved = alertRepo.save(alert);

        return AlertResponse.from(saved);
    }

    public AlertResponse getById( UUID id){
        Optional<Alert> alert = alertRepo.findById(id);
        return alert.map(AlertResponse::from).orElse(null);
    }


    private Incident createIncidentForAlert(AlertRequest req) {
        Incident i = new Incident();
        i.setTitle("Alert from " + req.source() + ": " + req.message());
        i.setSeverity(req.severity());
        i.setStatus(IncidentStatus.OPEN);
        return incidentRepo.save(i);
    }
}