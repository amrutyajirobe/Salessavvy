package com.salessavvy.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessavvy.auth.dto.RegisterRequest;
import com.salessavvy.auth.dto.UserRegistrationResponse;
import com.salessavvy.auth.service.RegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(
            RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        try {
            UserRegistrationResponse registeredUser =
                    registrationService.registerUser(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "User registered successfully",
                            "user", registeredUser
                    ));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error", exception.getMessage()
                    ));
        }
    }
}