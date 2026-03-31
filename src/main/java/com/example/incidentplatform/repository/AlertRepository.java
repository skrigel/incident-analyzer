package com.example.incidentplatform.repository;

import com.example.incidentplatform.domain.Alert;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.*;

public interface AlertRepository
        extends JpaRepository<Alert, UUID> {


    // Ordered by firedAt DESC so most recent alert is first.
    @Query("""
        SELECT a FROM Alert a
        WHERE a.incident.id = :id
        ORDER BY a.firedAt DESC
        """)
    List<Alert> findByIncidentIdOrderByFiredAtDesc(@Param("id") UUID incidentId);

    @Query("""
        SELECT a FROM Alert a
        WHERE a.incident.id = :incidentId
        """)
    List<Alert> findByIncidentId(
            @Param("incidentId") UUID incidentId);

    @Query("""
        SELECT a FROM Alert a
        WHERE a.incident IN :incidentIds
        ORDER BY a.firedAt DESC
        """)
    List<Alert> findByIncidentIdIn(
            @Param("incidentIds") Collection<UUID> incidentIds);

    // ── Used by AlertService correlation logic ───────────────────────
    // Counts recent alerts from a source to decide severity escalation.
    @Query("""
        SELECT COUNT(a) FROM Alert a
        WHERE a.source    = :source
          AND a.firedAt  >= :since
        """)
    long countBySourceSince(
            @Param("source") String  source,
            @Param("since")  Instant since);

    @Modifying
    @Query("DELETE FROM Alert a WHERE a.incident.id = :incidentId")
    void deleteByIncidentId(@Param("incidentId") UUID incidentId);
}