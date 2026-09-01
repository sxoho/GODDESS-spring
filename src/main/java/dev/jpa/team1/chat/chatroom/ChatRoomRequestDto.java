package dev.jpa.team1.chat.chatroom;

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
public class ChatRoomRequestDto {
    @NotNull
    private Long memberId;

    private Long productId;

    private Long orderId;

    @NotBlank
    @Size(max = 50)
    private String chatType;
}
