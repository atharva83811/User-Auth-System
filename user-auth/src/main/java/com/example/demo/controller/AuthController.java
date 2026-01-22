package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.TokenBlacklistService;
import com.example.demo.service.AuthService;
import com.example.demo.service.RefreshTokenService;

import jakarta.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;
	private final UserRepository userRepository;
	private final TokenBlacklistService blacklistService;

	@Value("${jwt.refresh-token-expiry-ms}")
	private long refreshExpiry;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthService authService,
			UserRepository userRepo, RefreshTokenService refreshTokenService, TokenBlacklistService tbs) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.authService = authService;
		this.refreshTokenService = refreshTokenService;
		this.userRepository = userRepo;
		this.blacklistService = tbs;
	}

	@PostMapping("/login")
	public TokenResponse login(@RequestBody LoginRequest request) {

		var auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

		var principal = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

		var user = userRepository.findByEmail(principal.getUsername()).orElseThrow();

		String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

		var refreshToken = refreshTokenService.create(user, refreshExpiry);

		return new TokenResponse(accessToken, refreshToken.getToken());
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void register(@Valid @RequestBody RegisterRequest request) {
		authService.register(request.email(), request.password());
	}

	@PostMapping("/refresh")
	public TokenResponse refresh(@RequestBody RefreshRequest request) {

		var refreshToken = refreshTokenService.validate(request.refreshToken());

		User user = refreshToken.getUser();

		String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

		return new TokenResponse(newAccessToken, request.refreshToken());
	}

	@PostMapping("/logout")
	public void logout(@RequestHeader("Authorization") String authHeader, @RequestBody RefreshRequest request) {

		String token = authHeader.substring(7);

		long expiryMillis = jwtUtil.getRemainingValidity(token);

		blacklistService.blacklist(token, expiryMillis);

		var refreshToken = refreshTokenService.validate(request.refreshToken());

		refreshTokenService.revoke(refreshToken);
	}

}
