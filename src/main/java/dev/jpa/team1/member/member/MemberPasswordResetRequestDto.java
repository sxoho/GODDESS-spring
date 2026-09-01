package dev.jpa.team1.member.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 비밀번호 재설정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberPasswordResetRequestDto {

    /**
     * 로그인 아이디
     */
    private String loginId;


    /**
     * 이메일
     */
    private String email;


    /**
     * 새로운 비밀번호
     */
    private String newPassword;
}