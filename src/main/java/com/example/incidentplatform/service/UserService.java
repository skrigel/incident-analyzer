package com.example.incidentplatform.service;


import com.example.incidentplatform.domain.User;
import com.example.incidentplatform.dto.*;
import com.example.incidentplatform.exception.NotFoundException;
import com.example.incidentplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    @Transactional
    public UserResponse create(CreateUserRequest req) {
        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());

        User saved = userRepo.save(user);
        return UserResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User", id));

        return UserResponse.from(user);
    }


}


