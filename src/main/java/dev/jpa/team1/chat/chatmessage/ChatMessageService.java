package dev.jpa.team1.chat.chatmessage;

import dev.jpa.team1.chat.chatroom.ChatRoom;
import dev.jpa.team1.chat.chatroom.ChatRoomRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository
            chatMessageRepository;

    private final ChatRoomRepository
            chatRoomRepository;

    private final MemberRepository
            memberRepository;

    private final ChatbotAiService
            chatbotAiService;


    /*
     * =========================================
     * USER 메시지만 저장
     * =========================================
     */
    @Transactional
    public ChatMessageResponseDto
    sendUserMessage(

            Long memberId,

            Long roomId,

            ChatMessageSendRequestDto requestDto

    ) {

        ChatRoom chatRoom =
                getAccessibleActiveRoom(
                        memberId,
                        roomId
                );


        ChatMessage message =
                ChatMessage.builder()

                        .chatRoom(
                                chatRoom
                        )

                        .senderType(
                                "USER"
                        )

                        .message(
                                requestDto
                                        .getMessage()
                                        .trim()
                        )

                        .build();


        return ChatMessageResponseDto.from(

                chatMessageRepository.save(
                        message
                )
        );
    }


    /*
     * =========================================
     * 채팅방 메시지 전체 조회
     * =========================================
     */
    @Transactional(
            readOnly = true
    )
    public List<ChatMessageResponseDto>
    getMessages(

            Long memberId,

            Long roomId

    ) {

        /*
         * 해당 회원의 채팅방인지 먼저 확인
         */
        getAccessibleRoom(
                memberId,
                roomId
        );


        return chatMessageRepository

                .findByChatRoomIdOrderByIdAsc(
                        roomId
                )

                .stream()

                .map(
                        ChatMessageResponseDto::from
                )

                .toList();
    }


    /*
     * =========================================
     * BOT 메시지 저장
     * =========================================
     */
    @Transactional
    public ChatMessageResponseDto
    saveBotMessage(

            Long roomId,

            String answer

    ) {

        ChatRoom chatRoom =
                chatRoomRepository

                        .findById(
                                roomId
                        )

                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.CHAT_ROOM_NOT_FOUND
                                        )
                        );


        if (
                !"ACTIVE".equals(
                        chatRoom.getStatus()
                )
        ) {

            throw new CustomException(

                    ErrorCode.INVALID_INPUT_VALUE,

                    "종료된 챗봇 대화에는 메시지를 저장할 수 없습니다."
            );
        }


        ChatMessage message =
                ChatMessage.builder()

                        .chatRoom(
                                chatRoom
                        )

                        .senderType(
                                "BOT"
                        )

                        .message(
                                answer
                        )

                        .build();


        return ChatMessageResponseDto.from(

                chatMessageRepository.save(
                        message
                )
        );
    }


    /*
     * =========================================
     * AI 챗봇 질문
     *
     * 1. 회원 채팅방 확인
     * 2. USER 질문 저장
     * 3. FastAPI 호출
     * 4. BOT 답변 저장
     * 5. USER + BOT + ACTION 반환
     * =========================================
     */
    @Transactional
    public ChatbotAskResponseDto
    askChatbot(

            Long memberId,

            Long roomId,

            ChatbotAskRequestDto requestDto

    ) {

        /*
         * 회원이 접근 가능한
         * ACTIVE 채팅방인지 확인
         */
        ChatRoom chatRoom =
                getAccessibleActiveRoom(
                        memberId,
                        roomId
                );


        String question =
                requestDto
                        .getMessage()
                        .trim();


        /*
         * =====================================
         * USER 질문 저장
         * =====================================
         */
        ChatMessage userEntity =
                ChatMessage.builder()

                        .chatRoom(
                                chatRoom
                        )

                        .senderType(
                                "USER"
                        )

                        .message(
                                question
                        )

                        .build();


        ChatMessageResponseDto
                userMessage =
                ChatMessageResponseDto.from(

                        chatMessageRepository.save(
                                userEntity
                        )
                );


        /*
         * =====================================
         * FastAPI -> Ollama 질문
         * =====================================
         */
        ChatbotAiService.AiAnswer
                aiAnswer =
                chatbotAiService.ask(

                        roomId,

                        question
                );


        /*
         * =====================================
         * BOT 답변 저장
         * =====================================
         */
        ChatMessage botEntity =
                ChatMessage.builder()

                        .chatRoom(
                                chatRoom
                        )

                        .senderType(
                                "BOT"
                        )

                        .message(
                                aiAnswer.getMessage()
                        )

                        .build();


        ChatMessageResponseDto
                botMessage =
                ChatMessageResponseDto.from(

                        chatMessageRepository.save(
                                botEntity
                        )
                );


        /*
         * =====================================
         * React 응답
         * =====================================
         */
        return ChatbotAskResponseDto

                .builder()

                .userMessage(
                        userMessage
                )

                .botMessage(
                        botMessage
                )

                .action(
                        aiAnswer.getAction()
                )

                .build();
    }


    /*
     * =========================================
     * ACTIVE 채팅방 검사
     * =========================================
     */
    private ChatRoom
    getAccessibleActiveRoom(

            Long memberId,

            Long roomId

    ) {

        ChatRoom chatRoom =
                getAccessibleRoom(
                        memberId,
                        roomId
                );


        if (
                !"ACTIVE".equals(
                        chatRoom.getStatus()
                )
        ) {

            throw new CustomException(

                    ErrorCode.INVALID_INPUT_VALUE,

                    "종료된 챗봇 대화에는 메시지를 보낼 수 없습니다."
            );
        }


        return chatRoom;
    }


    /*
     * =========================================
     * 채팅방 접근 권한 검사
     * =========================================
     */
    private ChatRoom
    getAccessibleRoom(

            Long memberId,

            Long roomId

    ) {

        /*
         * 회원 존재 여부 확인
         */
        memberRepository

                .findById(
                        memberId
                )

                .orElseThrow(
                        () ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                );


        /*
         * 채팅방 조회
         */
        ChatRoom chatRoom =
                chatRoomRepository

                        .findById(
                                roomId
                        )

                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.CHAT_ROOM_NOT_FOUND
                                        )
                        );


        /*
         * 다른 회원 채팅방 접근 방지
         */
        if (
                !chatRoom
                        .getMember()
                        .getId()
                        .equals(
                                memberId
                        )
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_CHAT_ACCESS
            );
        }


        return chatRoom;
    }
}