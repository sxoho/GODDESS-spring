package dev.jpa.team1.chat.chatbotfaq;

import dev.jpa.team1.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/faqs")
@RequiredArgsConstructor
public class ChatbotFaqController {

    private final ChatbotFaqService
            chatbotFaqService;

    @GetMapping
    public ResponseEntity<
            ApiResponse<
                    List<ChatbotFaqResponseDto>
            >
            >
    getFaqList(

            @RequestParam(
                    name = "category",
                    required = false
            )
            String category

    ) {

        return ResponseEntity.ok(

                ApiResponse.success(

                        chatbotFaqService
                                .getVisibleFaqs(
                                        category
                                )
                )
        );
    }
}