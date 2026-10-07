package dev.jpa.team1.chat.chatbotfaq;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatbotFaqRepository
        extends JpaRepository<ChatbotFaq, Long> {

    List<ChatbotFaq>
    findByVisibleOrderByDisplayOrderAscIdAsc(
            String visible
    );

    List<ChatbotFaq>
    findByCategoryAndVisibleOrderByDisplayOrderAscIdAsc(
            String category,
            String visible
    );
}