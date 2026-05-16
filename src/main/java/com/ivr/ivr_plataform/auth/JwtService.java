package com.ivr.ivr_plataform.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // KEY BASE64
    private static final String SECRET_KEY = "ZGQ5Y2Y0NjA5MmY0N2I4YWE4YjY0NzQ2ZmQ2MzYxNzQ5ZDM4YmM0M2QxZmM0YjA2YjY2YzI4MWM4YWE5Yg==";

    // GENERAR TOKEN
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // EXTRAER USERNAME
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    // VALIDAR TOKEN
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // TOKEN EXPIRADO
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    // EXTRAER EXPIRATION
    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    // EXTRAER CLAIM
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    // EXTRAER TODOS LOS CLAIMS
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // OBTENER KEY
    private Key getSignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
