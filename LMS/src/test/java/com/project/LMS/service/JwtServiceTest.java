package com.project.LMS.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    public void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        String token = jwtService.generateToken(claims, userDetails);

        Claims extractedClaims = Jwts.parserBuilder()
                .setSigningKey(jwtService.getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("USER", extractedClaims.get("role"));
        assertEquals(userDetails.getUsername(), extractedClaims.getSubject());
    }

    @Test
    public void testGenerateTokenWithoutExtraClaims() {
        String token = jwtService.generateToken(userDetails);
        Claims extractedClaims = Jwts.parserBuilder()
                .setSigningKey(jwtService.getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(userDetails.getUsername(), extractedClaims.getSubject());
    }

    @Test
    public void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenValid_InvalidUser() {
        String token = jwtService.generateToken(userDetails);
        UserDetails anotherUser = User.builder()
                .username("anotherUser")
                .password("password")
                .roles("USER")
                .build();
        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }

    @Test
    public void testIsTokenExpired() {
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        assertTrue(jwtService.isTokenExpired(token));
    }

    @Test
    public void testIsTokenExpired_Valid() {
        String token = jwtService.generateToken(userDetails);
        assertFalse(jwtService.isTokenExpired(token));
    }
}
