package com.prasanna.backendapp.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // 🔐 Secret key (at least 32 chars)
    private static final String SECRET = "mysecretkeymysecretkeymysecretkey";

    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 🔥 Generate JWT Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔥 Validate Token & get username
    public static String validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // username return
    }
}