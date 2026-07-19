package com.salessavvy.auth.dto;

import com.salessavvy.auth.entity.Role;

public record UserRegistrationResponse(
        Integer userId,
        String username,
        String email,
        Role role
) {
}