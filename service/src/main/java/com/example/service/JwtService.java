package com.example.service;

import com.example.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Секретный ключ (лучше вынести в application.yml)
    @Value("${token.signing.key:5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437}")
    private String jwtSigningKey;

    // Генерируем токен на основе данных пользователя
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().name());
        claims.put("status", user.getStatus().name());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 часа
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Извлекаем имя пользователя из токена
    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Проверка валидности (не протух ли он)
    public boolean isTokenValid(String token) {
        return extractAllClaims(token).getExpiration().after(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}