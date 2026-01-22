package com.example.demo.service;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public RefreshToken create(User user, long expiryMs) {

		RefreshToken token = new RefreshToken();
		token.setUser(user);
		token.setToken(UUID.randomUUID().toString());
		token.setExpiry(Instant.now().plusMillis(expiryMs));
		token.setRevoked(false);

		return refreshTokenRepository.save(token);
	}

	public RefreshToken validate(String token) {

		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid refresh token"));

		if (refreshToken.getRevoked()) {
			throw new RuntimeException("Refresh token revoked");
		}

		if (refreshToken.getExpiry().isBefore(Instant.now())) {
			throw new RuntimeException("Refresh token expired");
		}

		return refreshToken;
	}

	public void revoke(RefreshToken token) {
		token.setRevoked(true);
		refreshTokenRepository.save(token);
	}
}
