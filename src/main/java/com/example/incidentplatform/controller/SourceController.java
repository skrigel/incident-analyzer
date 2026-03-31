package com.example.incidentplatform.controller;

import com.example.incidentplatform.dto.CreateSourceRequest;
import com.example.incidentplatform.dto.SourceResponse;
import com.example.incidentplatform.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SourceResponse create(@Valid @RequestBody CreateSourceRequest request) {
        return sourceService.create(request);
    }

    @GetMapping
    public List<SourceResponse> listAll() {
        return sourceService.listAll();
    }

    @GetMapping("/{id}")
    public SourceResponse getById(@PathVariable UUID id) {
        return sourceService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        sourceService.delete(id);
    }
}