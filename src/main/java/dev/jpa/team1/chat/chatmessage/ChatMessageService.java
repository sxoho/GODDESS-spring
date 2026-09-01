package dev.jpa.team1.chat.chatmessage;

import dev.jpa.team1.chat.chatroom.ChatRoom;
import dev.jpa.team1.chat.chatroom.ChatRoomRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatMessageResponseDto sendMessage(String loginId, Long roomId, ChatMessageSendRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!chatRoom.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.INVALID_CHAT_ACCESS);
        }
        if (!"ACTIVE".equals(chatRoom.getStatus())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "종료된 채팅방에는 메시지를 보낼 수 없습니다.");
        }

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderType("USER")
                .sender(member)
                .message(requestDto.getMessage())
                .messageType(requestDto.getMessageType())
                .isRead("N") // 초기 읽음 상태 N
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);
        return ChatMessageResponseDto.from(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getMessages(String loginId, Long roomId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!chatRoom.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.INVALID_CHAT_ACCESS);
        }

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByIdAsc(roomId);
        return messages.stream()
                .map(ChatMessageResponseDto::from)
                .collect(Collectors.toList());
    }
}