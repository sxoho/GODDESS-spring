package dev.jpa.team1.chat.chatbotfaq;

import dev.jpa.team1.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat/faqs")
@RequiredArgsConstructor
public class ChatbotFaqController {

    private final ChatbotFaqService chatbotFaqService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatbotFaqResponseDto>>> getFaqList() {
        List<ChatbotFaqResponseDto> response = chatbotFaqService.getVisibleFaqs();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}