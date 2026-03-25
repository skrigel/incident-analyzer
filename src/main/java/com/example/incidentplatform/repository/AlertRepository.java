package com.example.incidentplatform.repository;

import com.example.incidentplatform.domain.Alert;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.*;

public interface AlertRepository
        extends JpaRepository<Alert, UUID> {


    @Query("""
        SELECT a FROM Alert a
        WHERE a.id==:id
        """)
    Optional<Alert> findById(
            @Param("incidentId") UUID id);
    // Ordered by firedAt DESC so most recent alert is first.
    List<Alert> findByIncidentIdOrderByFiredAtDesc(UUID incidentId);

    @Query("""
        SELECT a FROM Alert a
        WHERE a.incident==:incidentId
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
    @Query("DELETE FROM Alert a WHERE a.incident= :incidentId")
    void deleteByIncidentId(@Param("incidentId") UUID incidentId);
}