package dev.jpa.team1.chat.chatmessage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms/{roomId}")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;


    /*
     * =========================================
     * USER 메시지 저장
     * =========================================
     */
    @PostMapping("/messages")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(

            @PathVariable("roomId")
            Long roomId,

            @RequestParam("memberId")
            Long memberId,

            @Valid
            @RequestBody
            ChatMessageSendRequestDto requestDto

    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        chatMessageService.sendUserMessage(
                                memberId,
                                roomId,
                                requestDto
                        )
                );
    }


    /*
     * =========================================
     * 채팅방 메시지 전체 조회
     * =========================================
     */
    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getMessages(

            @PathVariable("roomId")
            Long roomId,

            @RequestParam("memberId")
            Long memberId

    ) {

        return ResponseEntity.ok(

                chatMessageService.getMessages(
                        memberId,
                        roomId
                )
        );
    }


    /*
     * =========================================
     * AI 챗봇 질문
     *
     * USER 질문 저장
     * -> FastAPI
     * -> Ollama
     * -> BOT 답변 저장
     * =========================================
     */
    @PostMapping("/ask")
    public ResponseEntity<ChatbotAskResponseDto> askChatbot(

            @PathVariable("roomId")
            Long roomId,

            @RequestParam("memberId")
            Long memberId,

            @Valid
            @RequestBody
            ChatbotAskRequestDto requestDto

    ) {

        return ResponseEntity.ok(

                chatMessageService.askChatbot(
                        memberId,
                        roomId,
                        requestDto
                )
        );
    }
}