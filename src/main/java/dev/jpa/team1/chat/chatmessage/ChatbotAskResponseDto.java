package dev.jpa.team1.chat.chatmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotAskResponseDto {

    private ChatMessageResponseDto userMessage;

    private ChatMessageResponseDto botMessage;

    private ChatbotActionDto action;
}