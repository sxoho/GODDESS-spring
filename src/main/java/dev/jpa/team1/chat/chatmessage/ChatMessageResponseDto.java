package dev.jpa.team1.chat.chatmessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private Long id;

    private Long chatRoomId;

    private String senderType;

    private Long senderMemberId;

    private String senderName;

    private String message;

    private String messageType;

    private String isRead;

    private LocalDateTime createdAt;


    public static ChatMessageResponseDto from(ChatMessage chatMessage) {
        return new ChatMessageResponseDto(
                chatMessage.getId(),
                chatMessage.getChatRoom().getId(),
                chatMessage.getSenderType(),
                chatMessage.getSender() != null ? chatMessage.getSender().getId() : null,
                chatMessage.getSender() != null ? chatMessage.getSender().getName() : null,
                chatMessage.getMessage(),
                chatMessage.getMessageType(),
                chatMessage.getIsRead(),
                chatMessage.getCreatedAt()
        );
    }

}
