package com.example.bank_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class JWTService {
    private static final String SECRET = "696F68B7A681E887EE8F1AB7A13C9A4367CF9551494656A9A0B28F7C2F93A395";
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public SecretKey generateKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String jwt) {
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(generateKey()) // ✅ Use same key
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            return null; // Return null if token is invalid
        }
    }

    public boolean isValidToken(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(generateKey()) // ✅ Use same key
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.getExpiration().after(Date.from(Instant.now()));
        } catch (JwtException e) {
            return false;
        }
    }
}
