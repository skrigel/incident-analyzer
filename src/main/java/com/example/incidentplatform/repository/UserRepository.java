package com.example.incidentplatform.repository;

import com.example.incidentplatform.domain.Severity;
import com.example.incidentplatform.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface UserRepository
        extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}