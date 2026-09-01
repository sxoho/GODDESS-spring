package dev.jpa.team1.chat.chatroom;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRequestDto {

    private Long productId; // 특정 상품에 대한 문의일 경우
    private Long orderId;   // 특정 주문에 대한 문의일 경우

    @NotBlank(message = "상담 유형(CHATBOT / LIVE_CHAT)을 지정해주세요.")
    private String chatType;
}