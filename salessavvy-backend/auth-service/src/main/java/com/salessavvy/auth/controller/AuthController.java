package com.salessavvy.auth.controller;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salessavvy.auth.dto.LoginRequest;
import com.salessavvy.auth.dto.LoginResponse;
import com.salessavvy.auth.entity.AuthUser;
import com.salessavvy.auth.service.AuthService;
import com.salessavvy.auth.service.JwtService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final boolean secureCookie;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            @Value("${app.auth-cookie-secure:false}")
            boolean secureCookie
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.secureCookie = secureCookie;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthUser user = authService.authenticate(request.username(), request.password());
        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie
                .from("authToken", token)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        LoginResponse response = new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                "Login successful"
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        cookie.toString()
                )
                .body(response);
    }
}