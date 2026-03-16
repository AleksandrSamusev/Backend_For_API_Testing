package dev.practice.shopapp.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${shopapp.jwt.secret}")
    private String jwtSecret;

    @Value("${shopapp.jwt.expirationMs}")
    private int jwtExpirationMs;

    // 🚀 1. GENERATE: Create the digital "Clearance Badge"
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🚀 2. PARSE: New 0.12+ syntax using verifyWith() and getPayload()
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey()) // 🚀 verifyWith replaces setSigningKey
                .build()
                .parseSignedClaims(token) // 🚀 parseSignedClaims replaces parseClaimsJws
                .getPayload() // 🚀 getPayload replaces getBody
                .getSubject();
    }

    // 🚀 3. VALIDATE: Updated for 0.12+
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
