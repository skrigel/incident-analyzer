package com.example.incidentplatform.controller;

import com.example.incidentplatform.dto.AlertRequest;
import com.example.incidentplatform.dto.AlertResponse;
import com.example.incidentplatform.dto.CreateIncidentRequest;
import com.example.incidentplatform.service.AlertService;
import com.example.incidentplatform.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@Validated
public class AlertController {

    private final AlertService service;

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public AlertResponse ingestAlert(@Valid @RequestBody AlertRequest req) {
        return service.ingest(req);
    }
}
