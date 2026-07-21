package com.salessavvy.auth.dto;

import com.salessavvy.auth.entity.Role;

public record LoginResponse(
        Integer userId,
        String username,
        Role role,
        String message
) {
}