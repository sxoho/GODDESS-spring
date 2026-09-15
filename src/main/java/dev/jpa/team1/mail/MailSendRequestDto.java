package dev.jpa.team1.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailSendRequestDto {

    private String to;
    private String title;
    private String content;
}