package dev.jpa.team1.member.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    @NotBlank
    @Size(max = 50)
    private String loginId;

    @NotBlank
    @Size(max = 255)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String nickname;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String tel;

    @Size(max = 10)
    private String zipcode;

    @Size(max = 255)
    private String address1;

    @Size(max = 255)
    private String address2;
}
