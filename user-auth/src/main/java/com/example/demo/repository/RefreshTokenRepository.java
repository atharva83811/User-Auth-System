package com.example.demo.repository;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	@Modifying
	@Query("""
			    DELETE FROM RefreshToken rt
			    WHERE rt.expiry < :now OR rt.revoked = true
			""")
	void deleteExpiredOrRevoked(Instant now);

	void deleteByUser(User user);
}
