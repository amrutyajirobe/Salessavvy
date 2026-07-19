package com.salessavvy.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salessavvy.auth.dto.RegisterRequest;
import com.salessavvy.auth.dto.UserRegistrationResponse;
import com.salessavvy.auth.entity.AuthUser;
import com.salessavvy.auth.entity.Role;
import com.salessavvy.auth.repository.AuthUserRepository;

@Service
public class RegistrationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder) {

        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserRegistrationResponse registerUser(RegisterRequest request) {

        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();

        if (authUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username is already taken");
        }

        if (authUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        AuthUser user = new AuthUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.password()));
        user.setRole(Role.CUSTOMER);

        AuthUser savedUser = authUserRepository.save(user);

        return new UserRegistrationResponse(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}