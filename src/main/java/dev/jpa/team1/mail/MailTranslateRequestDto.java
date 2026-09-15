package dev.jpa.team1.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailTranslateRequestDto {

    private String title;
    private String content;
    private String language;
}