package com.example.incidentplatform.transformer;

import com.example.incidentplatform.domain.AlertSource;
import com.example.incidentplatform.domain.Severity;
import com.example.incidentplatform.dto.AlertRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GitHubActionsTransformer implements AlertTransformer {

    @Override
    public AlertRequest transform(JsonNode payload, AlertSource source) {
        JsonNode workflowRun = payload.get("workflow_run");
        JsonNode workflow = payload.get("workflow");

        String workflowName = workflowRun != null ? workflowRun.get("name").asText() :
                             workflow != null ? workflow.get("name").asText() : "Unknown Workflow";
        String conclusion = workflowRun != null && workflowRun.has("conclusion") ?
                           workflowRun.get("conclusion").asText() : "unknown";
        String status = workflowRun != null ? workflowRun.get("status").asText() : "unknown";

        String message = String.format("GitHub Actions: %s - %s", workflowName, conclusion);
        Severity severity = mapConclusionToSeverity(conclusion);

        Instant firedAt = workflowRun != null && workflowRun.has("updated_at") ?
                         Instant.parse(workflowRun.get("updated_at").asText()) :
                         Instant.now();

        Map<String, String> metadata = new HashMap<>();
        if (payload.has("repository")) {
            metadata.put("repository", payload.get("repository").get("full_name").asText());
        }
        if (workflowRun != null && workflowRun.has("id")) {
            metadata.put("run_id", workflowRun.get("id").asText());
        }
        if (workflowRun != null && workflowRun.has("html_url")) {
            metadata.put("url", workflowRun.get("html_url").asText());
        }
        metadata.put("status", status);
        metadata.put("conclusion", conclusion);

        return new AlertRequest(
            UUID.randomUUID(),
            "github-actions",
            message,
            severity,
            firedAt,
            metadata
        );
    }

    @Override
    public boolean supports(String sourceType) {
        return "github".equalsIgnoreCase(sourceType) ||
               "github-actions".equalsIgnoreCase(sourceType);
    }

    private Severity mapConclusionToSeverity(String conclusion) {
        return switch (conclusion.toLowerCase()) {
            case "failure" -> Severity.MAJOR;
            case "timed_out" -> Severity.MAJOR;
            case "cancelled" -> Severity.MINOR;
            case "action_required" -> Severity.MINOR;
            case "success" -> Severity.LOW;
            default -> Severity.MINOR;
        };
    }
}
