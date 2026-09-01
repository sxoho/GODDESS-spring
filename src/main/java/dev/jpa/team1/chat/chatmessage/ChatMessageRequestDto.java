package dev.jpa.team1.chat.chatmessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    @NotNull
    private Long chatRoomId;

    @NotBlank
    @Size(max = 50)
    private String senderType;

    private Long senderMemberId;

    @NotBlank
    private String message;

    @NotBlank
    @Size(max = 50)
    private String messageType;
}
