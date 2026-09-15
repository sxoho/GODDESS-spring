package dev.jpa.team1.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailTranslateResponseDto {

    private Result result;

    @Getter
    @Setter
    public static class Result {
        private String title;
        private String content;
    }
}