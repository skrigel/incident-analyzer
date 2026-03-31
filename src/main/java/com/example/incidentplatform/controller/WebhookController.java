package com.example.incidentplatform.controller;

import com.example.incidentplatform.domain.AlertSource;
import com.example.incidentplatform.dto.AlertRequest;
import com.example.incidentplatform.dto.AlertResponse;
import com.example.incidentplatform.dto.WebhookResponse;
import com.example.incidentplatform.exception.NotFoundException;
import com.example.incidentplatform.repository.AlertSourceRepository;
import com.example.incidentplatform.service.AlertService;
import com.example.incidentplatform.transformer.AlertTransformer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final AlertSourceRepository sourceRepo;
    private final AlertService alertService;
    private final List<AlertTransformer> transformers;

    @PostMapping("/{sourceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WebhookResponse receiveWebhook(
        @PathVariable UUID sourceId,
        @RequestBody JsonNode payload
    ) {
        AlertSource source = sourceRepo.findByIdAndActiveTrue(sourceId)
            .orElseThrow(() -> new NotFoundException("AlertSource", sourceId));

        AlertTransformer transformer = transformers.stream()
            .filter(t -> t.supports(source.getSourceType()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "No transformer found for source type: " + source.getSourceType()
            ));

        AlertRequest alertRequest = transformer.transform(payload, source);
        AlertResponse alertResponse = alertService.ingest(alertRequest);

        return WebhookResponse.from(alertResponse);
    }
}
