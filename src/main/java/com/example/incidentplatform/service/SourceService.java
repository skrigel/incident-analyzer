package com.example.incidentplatform.service;

import com.example.incidentplatform.domain.AlertSource;
import com.example.incidentplatform.dto.CreateSourceRequest;
import com.example.incidentplatform.dto.SourceResponse;
import com.example.incidentplatform.exception.NotFoundException;
import com.example.incidentplatform.repository.AlertSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceService {

    private final AlertSourceRepository sourceRepo;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Transactional
    public SourceResponse create(CreateSourceRequest req) {
        AlertSource source = new AlertSource();
        source.setName(req.name());
        source.setSourceType(req.sourceType());
        source.setWebhookSecret(UUID.randomUUID().toString());
        source.setActive(true);
        source.setTransformConfig(req.transformConfig());

        AlertSource saved = sourceRepo.save(source);
        return SourceResponse.from(saved, baseUrl);
    }

    @Transactional(readOnly = true)
    public List<SourceResponse> listAll() {
        return sourceRepo.findAllActive().stream()
            .map(source -> SourceResponse.from(source, baseUrl))
            .toList();
    }

    @Transactional(readOnly = true)
    public SourceResponse getById(UUID id) {
        AlertSource source = sourceRepo.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new NotFoundException("AlertSource", id));
        return SourceResponse.from(source, baseUrl);
    }

    @Transactional
    public void delete(UUID id) {
        AlertSource source = sourceRepo.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new NotFoundException("AlertSource", id));
        source.setActive(false);
        sourceRepo.save(source);
    }
}