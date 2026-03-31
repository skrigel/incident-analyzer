package com.example.incidentplatform.repository;

import com.example.incidentplatform.domain.Incident;
import com.example.incidentplatform.domain.IncidentStatus;
import com.example.incidentplatform.domain.Severity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.*;

public interface IncidentRepository
        extends JpaRepository<Incident, UUID> {

    Optional<Incident> findFirstBySourceAndStatus(
             String source, IncidentStatus status);


    List<Incident> findByStatus(IncidentStatus status);

    List<Incident> findBySeverityAndStatus(
            Severity severity, IncidentStatus status);

    // Fetches assignedTo in the same JOIN so UserSummary mapping
    @Query("""

            SELECT i FROM Incident i
            LEFT JOIN FETCH i.assignedTo
            WHERE i.id = :id
        """)
    Optional<Incident> findByIdWithAssignee(@Param("id") UUID id);

    // Finds the most recent open incident from the same source
    @Query("""
         SELECT i FROM Incident i
        WHERE i.status = 'RESOLVED'
          AND i.resolvedAt >= :since
        ORDER BY i.resolvedAt DESC
        """)
    List<Incident> findRecentResolved(
            @Param("since") Instant since,
            Pageable pageable);
}
