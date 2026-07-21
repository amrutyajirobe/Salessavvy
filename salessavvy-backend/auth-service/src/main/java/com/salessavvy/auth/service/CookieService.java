package com.salessavvy.auth.service;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import com.salessavvy.auth.config.AppProperties;


@Service
public class CookieService {

    private final AppProperties appProperties;

    public CookieService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public ResponseCookie createCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(appProperties.authCookieSecure())
                .sameSite("Lax")
                .path("/")
                .build();
    }
}
