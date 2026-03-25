package com.example.incidentplatform.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Setter @Getter
@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID incident;

    @Column
    private String source;

    @Column
    private String message;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @CreationTimestamp
    private Instant createdAt;

    private Instant firedAt;
}
