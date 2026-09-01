package dev.jpa.team1.member.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private String grantType;

    private String accessToken;

    private String refreshToken;

    private Long expiresIn;
}