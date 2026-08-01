package com.salessavvy.product.security;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final SecretKey signingKey;
    public JwtService(@Value("${jwt.secret}") String encodedSecret) {
        byte[] bytes;
        try { bytes = Decoders.BASE64.decode(encodedSecret); }
        catch (IllegalArgumentException ex) { throw new IllegalStateException("JWT_SECRET must be valid Base64", ex); }
        if (bytes.length < 32) throw new IllegalStateException("JWT_SECRET must contain at least 32 random bytes");
        signingKey = Keys.hmacShaKeyFor(bytes);
    }
    public AuthenticatedUser parse(String token) {
        Claims claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
        return new AuthenticatedUser(claims.get("userId", Integer.class), claims.getSubject(), claims.get("role", String.class));
    }
    public boolean isValid(String token) {
        try { return parse(token).username() != null; }
        catch (JwtException | IllegalArgumentException ex) { return false; }
    }
}
