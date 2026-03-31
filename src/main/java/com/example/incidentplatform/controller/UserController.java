package com.example.incidentplatform.controller;


import com.example.incidentplatform.dto.CreateUserRequest;
import com.example.incidentplatform.dto.UserResponse;
import com.example.incidentplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/{id}")
    public UserResponse findUser(@PathVariable UUID id) {
        return service.getUser(id);
    }
}
