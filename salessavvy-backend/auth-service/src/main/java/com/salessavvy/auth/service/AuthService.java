package com.salessavvy.auth.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import com.salessavvy.auth.dto.LoginRequest;
import com.salessavvy.auth.entity.AuthUser;
import com.salessavvy.auth.repository.AuthUserRepository;

//import jakarta.validation.Valid;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder) {

        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthUser authenticate(String username, String rawPassword) {

        if (username == null || username.isBlank()
                || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }

        AuthUser user = authUserRepository
                .findByUsername(username.trim())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }

}