package dev.jpa.team1.chat.chatmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotAskResponseDto {

    private ChatMessageResponseDto userMessage;

    private ChatMessageResponseDto botMessage;

    private ChatbotActionDto action;

    /*
     * 챗봇이 추천하는
     * 실제 여신강림 등록 상품
     */
    private List<ChatbotRecommendedProductDto> recommendedProducts;
}