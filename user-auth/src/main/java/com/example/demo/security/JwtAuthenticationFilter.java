package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final TokenBlacklistService blacklistService;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService,
			TokenBlacklistService blacklistService) {

		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		this.blacklistService = blacklistService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {

			String token = authHeader.substring(7);

			// 🔥 Check Redis blacklist
			if (blacklistService.isBlacklisted(token)) {
				filterChain.doFilter(request, response);
				return;
			}

			try {
				String email = jwtUtil.validate(token).getBody().getSubject();

				UserDetails user = userDetailsService.loadUserByUsername(email);

				var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (Exception ignored) {
				// invalid token → request remains unauthenticated
			}
		}

		filterChain.doFilter(request, response);
	}
}
