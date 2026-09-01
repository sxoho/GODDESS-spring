package dev.jpa.team1.member.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
@SequenceGenerator(
        name = "REFRESH_TOKEN_SEQ_GEN",
        sequenceName = "refresh_token_seq",
        initialValue = 1,
        allocationSize = 1
)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFRESH_TOKEN_SEQ_GEN")
    @Column(name = "token_id")
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(name = "token", nullable = false, length = 500)
    private String token;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public RefreshToken(String loginId, String token) {
        this.loginId = loginId;
        this.token = token;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 토큰 갱신 시 DB 레코드 업데이트를 위한 비즈니스 메서드
    public void updateToken(String newToken) {
        this.token = newToken;
        this.updatedAt = LocalDateTime.now();
    }
}