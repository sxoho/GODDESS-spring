package dev.jpa.team1.member.login;

import dev.jpa.team1.member.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private Long memberId;
    private String loginId;
    private String name;
    private String nickname;

    /**
     * 회원 등급
     */
    private Long gradeId;
    private String gradeName;
    private String status;

    /**
     * JWT
     */
    private String accessToken;
    private String refreshToken;


    public static LoginResponseDto from(
            Member member,
            String accessToken,
            String refreshToken
    ) {

        return new LoginResponseDto(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getNickname(),

                member.getGrade() != null
                        ? member.getGrade().getId()
                        : null,

                member.getGrade() != null
                        ? member.getGrade().getName()
                        : null,

                member.getStatus(),

                accessToken,
                refreshToken
        );
    }
}