package com.greenwich.ecommerce.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenService {

    private final String jwtSecret = "testSecretKey"; // Đổi bằng biến môi trường khi production
    private final long jwtExpirationMs = 3600_000; // 1 giờ

    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // thường là email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
