package com.desarrollox.backend_stranger_drug.api_login.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Jwts;

@Service
@RequiredArgsConstructor
public class ServiceToken implements IServiceToken{

    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Override
    public String generateAccessToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRole().name());
        claims.put("birthdate", user.getBirthdate());
        claims.put("type", "access");

        return createToken(claims, user.getEmail(), jwtExpiration);
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("type", "refresh");
        claims.put("jti", UUID.randomUUID().toString()); // JWT ID único

        return createToken(claims, user.getEmail(), refreshExpiration);
    }

    @Override
    public Long getTokenExpirationTime() {
        return jwtExpiration / 1000; // Retornar en segundos
    }

    @Override
    public boolean validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            
        return true;
    }
    
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    } 
}
