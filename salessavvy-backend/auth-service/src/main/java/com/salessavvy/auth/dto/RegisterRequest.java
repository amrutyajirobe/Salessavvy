package com.salessavvy.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


// Client sends JSON-format data. Spring converts JSON into a request DTO
// So, the controller receives it in RegistrationController as "/register"

//Register request DTO
public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 255,
              message = "Username must contain between 3 and 255 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8,
              message = "Password must contain at least 8 characters")
        String password

) {
}