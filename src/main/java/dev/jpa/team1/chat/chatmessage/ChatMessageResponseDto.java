package dev.jpa.team1.chat.chatmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {

    private Long id;

    private Long chatRoomId;

    private String senderType;

    private String message;

    private LocalDateTime createdAt;


    public static ChatMessageResponseDto from(
            ChatMessage chatMessage
    ) {

        return ChatMessageResponseDto
                .builder()

                .id(
                        chatMessage.getId()
                )

                .chatRoomId(
                        chatMessage
                                .getChatRoom()
                                .getId()
                )

                .senderType(
                        chatMessage.getSenderType()
                )

                .message(
                        chatMessage.getMessage()
                )

                .createdAt(
                        chatMessage.getCreatedAt()
                )

                .build();
    }
}