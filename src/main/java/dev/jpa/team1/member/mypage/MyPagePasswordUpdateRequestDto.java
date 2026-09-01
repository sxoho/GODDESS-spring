package dev.jpa.team1.member.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 마이페이지 비밀번호 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class MyPagePasswordUpdateRequestDto {

    /**
     * 현재 비밀번호
     */
    private String currentPassword;


    /**
     * 새 비밀번호
     */
    private String newPassword;
}