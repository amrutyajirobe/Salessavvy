package com.salessavvy.auth.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.salessavvy.auth.entity.AuthUser;
import com.salessavvy.auth.entity.Role;
import com.salessavvy.auth.repository.AuthUserRepository;
import com.salessavvy.auth.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthUserRepository authUserRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AuthUserRepository authUserRepository
    ) {
        this.jwtService = jwtService;
        this.authUserRepository = authUserRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = readAuthToken(request);

        if (token != null
                && jwtService.isTokenValid(token)
                && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            String username = jwtService.extractUsername(token);
            Role tokenRole = jwtService.extractRole(token);

            AuthUser user = authUserRepository
                    .findByUsername(username)
                    .orElse(null);

            if (user != null && user.getRole() == tokenRole) {
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                null,
                                List.of(authority)
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String readAuthToken(
            HttpServletRequest request
    ) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("authToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}