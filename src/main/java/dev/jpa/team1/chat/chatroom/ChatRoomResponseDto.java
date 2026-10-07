package dev.jpa.team1.chat.chatroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {

    private Long id;

    private Long memberId;

    private String memberNickname;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime closedAt;


    public static ChatRoomResponseDto from(
            ChatRoom chatRoom
    ) {

        return ChatRoomResponseDto
                .builder()

                .id(
                        chatRoom.getId()
                )

                .memberId(
                        chatRoom
                                .getMember()
                                .getId()
                )

                .memberNickname(
                        chatRoom
                                .getMember()
                                .getNickname()
                )

                .status(
                        chatRoom.getStatus()
                )

                .createdAt(
                        chatRoom.getCreatedAt()
                )

                .updatedAt(
                        chatRoom.getUpdatedAt()
                )

                .closedAt(
                        chatRoom.getClosedAt()
                )

                .build();
    }
}