package com.example.incidentplatform.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "incidents")
public class Incident {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable=false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status=IncidentStatus.OPEN;

    @Column(nullable=false)
    private String source;

    private String summary;

    private Set<String> tags;

    @OneToMany
    private List<Alert> alerts;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @CreationTimestamp
    private Instant createdAt;

    private Instant resolvedAt;


    public boolean isResolved() {
        return status == IncidentStatus.RESOLVED;
    }

    public void markResolved() {
        this.status = IncidentStatus.RESOLVED;
        this.resolvedAt = Instant.now();
    }


    public Collection<Object> getAlerts() {
        return new ArrayList<>(this.alerts);
    }

}

