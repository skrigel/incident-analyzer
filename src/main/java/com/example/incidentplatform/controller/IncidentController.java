package com.example.incidentplatform.controller;


import com.example.incidentplatform.dto.*;
import com.example.incidentplatform.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// TODO: add pagination
@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
@Validated
public class IncidentController {

    private final IncidentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponse create(
            @Valid @RequestBody CreateIncidentRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public IncidentResponse getById(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "false") boolean includeAlerts) {
        return service.getById(id, includeAlerts);
    }

    @PatchMapping("/{id}/status")
    public IncidentResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest req) {
        return service.updateStatus(id, req);
    }

    @PatchMapping("/{id}/assign")
    public IncidentResponse assign(
            @PathVariable UUID id,
            @Valid @RequestBody AssignRequest req) {
        return service.assign(id, req);
    }

    @GetMapping("/{id}/alerts")
    public List<AlertResponse> getIncidentAlerts(@PathVariable UUID id,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        return service.getAlerts(id);
    }
}
