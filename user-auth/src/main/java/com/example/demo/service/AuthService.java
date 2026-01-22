package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void register(String email, String password) {

		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email already registered");
		}

		User user = new User();
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(Role.USER);
		user.setEnabled(true);

		userRepository.save(user);
	}
}
