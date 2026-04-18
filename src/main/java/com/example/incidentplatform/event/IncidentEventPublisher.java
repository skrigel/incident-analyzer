package com.example.incidentplatform.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class IncidentEventPublisher {
    private final KafkaTemplate<String, IncidentCreatedEvent> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onIncidentCreated(IncidentCreatedEvent event) {
        kafkaTemplate.send(
                "incidents.created",
                event.incidentId().toString(),
                event
        );
    }
}
