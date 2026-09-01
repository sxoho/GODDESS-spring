package dev.jpa.team1.chat.chatbotfaq;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatbotFaqResponseDto {

    private Long id;

    private Long categoryId;

    private String question;

    private String answer;

    private Integer viewCount;

    private String visible;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ChatbotFaqResponseDto from(ChatbotFaq faq) {
        return new ChatbotFaqResponseDto(
                faq.getId(),
                faq.getCategoryId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getViewCount(),
                faq.getVisible(),
                faq.getCreatedAt(),
                faq.getUpdatedAt()
        );
    }
}
