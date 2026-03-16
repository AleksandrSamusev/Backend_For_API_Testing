package dev.practice.shopapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority; // 🚀 Added
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${shopapp.jwt.secret}")
    private String jwtSecret;

    @Value("${shopapp.jwt.expirationMs}")
    private int jwtExpirationMs;

    // 🚀 1. GENERATE: Now with embedded Roles (Clearance Level)
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        // 🛡️ THE FIX: Extract roles from the authorities collection
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("roles", roles) // 🛰️ Embed roles in the token payload
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey()) // SignatureAlgorithm is inferred from key type
                .compact();
    }

    // 🚀 2. PARSE: 0.12+ syntax
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 🚀 3. VALIDATE: 0.12+ syntax
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}