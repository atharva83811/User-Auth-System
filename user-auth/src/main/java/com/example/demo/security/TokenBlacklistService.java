package com.example.demo.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlacklistService {

	private final StringRedisTemplate redisTemplate;

	public TokenBlacklistService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void blacklist(String token, long ttlMillis) {
		redisTemplate.opsForValue().set(token, "BLACKLISTED", Duration.ofMillis(ttlMillis));
	}

	public boolean isBlacklisted(String token) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(token));
	}
}
