package com.example.incidentplatform.repository;

import com.example.incidentplatform.domain.AlertSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertSourceRepository extends JpaRepository<AlertSource, UUID> {

    @Query("SELECT s FROM AlertSource s WHERE s.active = true")
    List<AlertSource> findAllActive();

    Optional<AlertSource> findByIdAndActiveTrue(UUID id);

    Optional<AlertSource> findByWebhookSecret(String webhookSecret);
}