package dev.jpa.team1.chat.chatmessage;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageSendRequestDto {

    @NotBlank(message = "메시지 내용을 입력해주세요.")
    private String message;

    @NotBlank(message = "메시지 유형(TEXT / IMAGE / PRODUCT_CARD / BEAUTY_CARD)을 입력해주세요.")
    private String messageType;
}