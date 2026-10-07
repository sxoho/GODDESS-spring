package dev.jpa.team1.chat.chatmessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageSendRequestDto {

    @NotBlank(
            message = "메시지를 입력해주세요."
    )
    @Size(
            max = 2000,
            message =
                    "메시지는 2000자 이내로 입력해주세요."
    )
    private String message;
}