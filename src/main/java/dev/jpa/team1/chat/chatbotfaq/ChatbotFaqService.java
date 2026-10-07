package dev.jpa.team1.chat.chatbotfaq;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotFaqService {

    private final ChatbotFaqRepository
            chatbotFaqRepository;

    @Transactional(readOnly = true)
    public List<ChatbotFaqResponseDto>
    getVisibleFaqs(
            String category
    ) {

        List<ChatbotFaq> faqs;

        if (
                category == null ||
                category.isBlank()
        ) {

            faqs =
                    chatbotFaqRepository
                            .findByVisibleOrderByDisplayOrderAscIdAsc(
                                    "Y"
                            );

        } else {

            faqs =
                    chatbotFaqRepository
                            .findByCategoryAndVisibleOrderByDisplayOrderAscIdAsc(
                                    category
                                            .trim()
                                            .toUpperCase(),
                                    "Y"
                            );
        }

        return faqs
                .stream()
                .map(
                        ChatbotFaqResponseDto::from
                )
                .toList();
    }
}