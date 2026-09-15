package dev.jpa.team1.mail;

import dev.jpa.team1.mail.MailSendRequestDto;
import dev.jpa.team1.mail.MailTranslateRequestDto;
import dev.jpa.team1.mail.MailTranslateResponseDto;
import dev.jpa.team1.mail.MailService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/translate")
    public ResponseEntity<MailTranslateResponseDto> translate(
            @RequestBody MailTranslateRequestDto request
    ) {

        MailTranslateResponseDto result =
                mailService.translate(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(
            @RequestBody MailSendRequestDto request
    ) {

        mailService.send(request);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "메일이 정상적으로 발송되었습니다."
                )
        );
    }
}