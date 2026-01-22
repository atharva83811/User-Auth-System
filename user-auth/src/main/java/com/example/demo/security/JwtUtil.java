package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	private final Key key;
	private final long expiryMs;
	private final String secret;

	public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.access-token-expiry-ms}") long expiryMs) {

		this.secret = secret;
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expiryMs = expiryMs;
	}

	public String generateToken(String email, String role) {
		return Jwts.builder().setSubject(email).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiryMs)).signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Jws<Claims> validate(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	public long getRemainingValidity(String token) {
		Date expiry = validate(token).getBody().getExpiration();
		return expiry.getTime() - System.currentTimeMillis();
	}
}
