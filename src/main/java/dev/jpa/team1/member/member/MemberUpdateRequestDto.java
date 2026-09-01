package dev.jpa.team1.member.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequestDto {

    private String nickname;

    /**
     * 빈 문자열이면
     * 기존 비밀번호 유지
     */
    private String password;

    private String email;

    /**
     * 1 = USER
     * 2 = GOLD
     * 3 = VIP
     * 4 = ADMIN
     */
    private Long gradeId;

    private String zipcode;

    private String address1;

    private String address2;
}