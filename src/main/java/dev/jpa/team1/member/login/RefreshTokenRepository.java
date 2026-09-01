package dev.jpa.team1.member.login;

import dev.jpa.team1.member.login.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByLoginId(String loginId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByLoginId(String loginId);
}