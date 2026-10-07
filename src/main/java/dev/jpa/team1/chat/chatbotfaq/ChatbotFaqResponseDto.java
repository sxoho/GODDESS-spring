package dev.jpa.team1.chat.chatbotfaq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatbotFaqResponseDto {

    private Long id;
    private String category;
    private String question;
    private String answer;
    private String keywords;
    private Integer displayOrder;

    public static ChatbotFaqResponseDto from(
            ChatbotFaq faq
    ) {

        return ChatbotFaqResponseDto
                .builder()
                .id(faq.getId())
                .category(faq.getCategory())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .keywords(faq.getKeywords())
                .displayOrder(
                        faq.getDisplayOrder()
                )
                .build();
    }
}