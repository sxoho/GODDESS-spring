package dev.jpa.team1.chat.chatbotfaq;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotFaqService {

    private final ChatbotFaqRepository chatbotFaqRepository;

    @Transactional(readOnly = true)
    public List<ChatbotFaqResponseDto> getVisibleFaqs() {
        // visible 컬럼이 'Y'인 FAQ만 조회하여 반환
        List<ChatbotFaq> faqs = chatbotFaqRepository.findByVisibleOrderByIdDesc("Y");
        return faqs.stream()
                .map(ChatbotFaqResponseDto::from)
                .collect(Collectors.toList());
    }
}