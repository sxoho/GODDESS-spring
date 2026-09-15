package dev.jpa.team1.mail;

import dev.jpa.team1.mail.MailSendRequestDto;
import dev.jpa.team1.mail.MailTranslateRequestDto;
import dev.jpa.team1.mail.MailTranslateResponseDto;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${goddess.fastapi.mail-url}")
    private String fastApiMailUrl;

    @Value("${goddess.fastapi.key}")
    private String fastApiKey;

    public MailTranslateResponseDto translate(
            MailTranslateRequestDto request
    ) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();

        body.put("title", request.getTitle());
        body.put("content", request.getContent());
        body.put("language", request.getLanguage());

        body.put(
                "SpringBoot_FastAPI_KEY",
                fastApiKey
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(
                        body,
                        headers
                );

        ResponseEntity<MailTranslateResponseDto> response =
                restTemplate.exchange(
                        fastApiMailUrl,
                        HttpMethod.POST,
                        entity,
                        MailTranslateResponseDto.class
                );

        return response.getBody();
    }

    public void send(
            MailSendRequestDto request
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(from);
            helper.setTo(request.getTo());
            helper.setSubject(request.getTitle());

            helper.setText(
                    request.getContent(),
                    true
            );

            mailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(
                    "메일 발송 중 오류가 발생했습니다.",
                    e
            );
        }
    }
}