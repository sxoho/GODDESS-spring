package dev.jpa.team1.member.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPageUpdateRequestDto {

    private String loginId;

    private String nickname;

    private String zipcode;

    private String address1;

    private String address2;

    private String skinType;

    private String personalColor;
}