package com.salessavvy.product.security;
public record AuthenticatedUser(Integer userId, String username, String role) {}
