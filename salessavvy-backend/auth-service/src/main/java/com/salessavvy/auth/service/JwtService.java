package com.salessavvy.auth.service;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.salessavvy.auth.entity.AuthUser;
import com.salessavvy.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMilliseconds;

    public JwtService(
            @Value("${jwt.secret}") String encodedSecret,
            @Value("${jwt.expiration-milliseconds}")
            long expirationMilliseconds
    ) {
        byte[] secretBytes;

        try {
            secretBytes = Decoders.BASE64.decode(encodedSecret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "JWT_SECRET must be a valid Base64 value",
                    exception
            );
        }

        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must contain at least 32 random bytes"
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
        this.expirationMilliseconds = expirationMilliseconds;
    }

    public String generateToken(AuthUser user) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(
                issuedAt.getTime() + expirationMilliseconds
        );

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("role", user.getRole().name())
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Role extractRole(String token) {
        String role = extractClaims(token)
                .get("role", String.class);

        return Role.valueOf(role);
    }

    public Integer extractUserId(String token) {
        return extractClaims(token)
                .get("userId", Integer.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);

            return claims.getSubject() != null
                    && claims.getExpiration() != null
                    && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}