package dev.jpa.team1.member.loginhistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryResponseDto {
    private Long id;

    private Long memberId;

    private String success;

    private String ip;

    private String device;

    private LocalDateTime createdAt;

    private LocalDateTime logoutDate;


    public static LoginHistoryResponseDto from(LoginHistory history) {
        return new LoginHistoryResponseDto(
                history.getId(),
                history.getMember().getId(),
                history.getSuccess(),
                history.getIp(),
                history.getDevice(),
                history.getCreatedAt(),
                history.getLogoutDate()
        );
    }

}
