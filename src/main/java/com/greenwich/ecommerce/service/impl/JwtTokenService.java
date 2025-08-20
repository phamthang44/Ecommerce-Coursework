package com.greenwich.ecommerce.service.impl;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final Dotenv dotenv = Dotenv.load();
    private final String SECRET_KEY = dotenv.get("JWT_SECRET");
    private final long jwtExpirationMs = Long.parseLong(dotenv.get("JWT_EXPIRATION_MS"));
    private final RedisServiceImpl redisService;
    private static final String TOKEN_BLACKLIST_PREFIX = "blacklisted_token:";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Try both the subject and the "mail" claim
        String email = claims.getSubject();
        if (email == null || email.isEmpty()) {
            email = claims.get("mail", String.class);
        }
        return email;
    }

    public Boolean validateToken(String token) {
        if (null == token) return false;

        if (redisService.exists(TOKEN_BLACKLIST_PREFIX + token)) {
            log.info("Token is blacklisted");
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parse(token);
            return true;
        } catch (MalformedJwtException
                 | ExpiredJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && !isExpired(token);
    }

    public boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

//    public boolean isValid(String token, UserDetails userDetails) {
//        String username = extractUsername(token);
//        return username.equals(userDetails.getUsername());
//    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
