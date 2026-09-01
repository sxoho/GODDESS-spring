package dev.jpa.team1.chat.chatroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long id;

    private Long memberId;

    private String memberName;

    private Long counselorId;

    private Long productId;

    private Long orderId;

    private String chatType;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime closedDate;


    public static ChatRoomResponseDto from(ChatRoom chatRoom) {
        return new ChatRoomResponseDto(
                chatRoom.getId(),
                chatRoom.getMember().getId(),
                chatRoom.getMember().getName(),
                chatRoom.getCounselor() != null ? chatRoom.getCounselor().getId() : null,
                chatRoom.getProduct() != null ? chatRoom.getProduct().getId() : null,
                chatRoom.getOrders() != null ? chatRoom.getOrders().getId() : null,
                chatRoom.getChatType(),
                chatRoom.getStatus(),
                chatRoom.getCreatedAt(),
                chatRoom.getClosedDate()
        );
    }

}
