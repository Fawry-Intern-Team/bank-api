package com.example.bank_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {
    @Test
    public void generateToken() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // safe default
        System.out.println(DatatypeConverter.printHexBinary(key.getEncoded()));
        String jwt = Jwts.builder()
                .setSubject("mohamed")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }
}