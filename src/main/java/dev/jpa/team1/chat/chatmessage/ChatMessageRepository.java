package dev.jpa.team1.chat.chatmessage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 채팅 메시지 Repository
 *
 * CHAT_MESSAGE 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 채팅방 안에서 회원, 상담원 또는 챗봇이 전송한
 * 메시지와 읽음 여부를 관리한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST  http://localhost:포트번호/api/chat-rooms/{chatRoomId}/messages
 * GET   http://localhost:포트번호/api/chat-rooms/{chatRoomId}/messages
 * PATCH http://localhost:포트번호/api/chat-messages/{messageId}/read
 */
public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    /**
     * 특정 채팅방의 전체 메시지 조회
     *
     * 오래된 메시지부터 시간순으로 조회한다.
     */
    List<ChatMessage>
            findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    /**
     * 특정 채팅방의 메시지를 최근 메시지부터 조회
     */
    List<ChatMessage>
            findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    /**
     * 특정 채팅방의 발신자 유형별 메시지 조회
     *
     * senderType 예시:
     * MEMBER, COUNSELOR, CHATBOT
     */
    List<ChatMessage>
            findByChatRoomIdAndSenderTypeOrderByCreatedAtAsc(
                    Long chatRoomId,
                    String senderType
            );

    /**
     * 특정 회원이 보낸 메시지 조회
     *
     * ChatMessage Entity의 발신 회원 필드명은 sender이다.
     */
    List<ChatMessage>
            findBySenderIdOrderByCreatedAtDesc(Long senderId);

    /**
     * 특정 채팅방에서 특정 회원이 보낸 메시지 조회
     */
    List<ChatMessage>
            findByChatRoomIdAndSenderIdOrderByCreatedAtAsc(
                    Long chatRoomId,
                    Long senderId
            );

    /**
     * 특정 채팅방의 메시지 유형별 조회
     *
     * messageType 예시:
     * TEXT, IMAGE, FILE, SYSTEM
     */
    List<ChatMessage>
            findByChatRoomIdAndMessageTypeOrderByCreatedAtAsc(
                    Long chatRoomId,
                    String messageType
            );

    /**
     * 특정 채팅방의 읽음 여부별 메시지 조회
     *
     * isRead 예시:
     * Y = 읽음
     * N = 읽지 않음
     */
    List<ChatMessage>
            findByChatRoomIdAndIsReadOrderByCreatedAtAsc(
                    Long chatRoomId,
                    String isRead
            );

    /**
     * 특정 채팅방에서 발신자 유형과 읽음 여부가
     * 모두 일치하는 메시지 조회
     */
    List<ChatMessage>
            findByChatRoomIdAndSenderTypeAndIsReadOrderByCreatedAtAsc(
                    Long chatRoomId,
                    String senderType,
                    String isRead
            );

    /**
     * 특정 채팅방의 가장 최근 메시지 한 건 조회
     *
     * 채팅방 목록에 마지막 메시지를 표시할 때 사용할 수 있다.
     */
    Optional<ChatMessage>
            findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    /**
     * 특정 채팅방에서 특정 발신자 유형이 보낸
     * 가장 최근 메시지 한 건 조회
     */
    Optional<ChatMessage>
            findFirstByChatRoomIdAndSenderTypeOrderByCreatedAtDesc(
                    Long chatRoomId,
                    String senderType
            );

    /**
     * 특정 채팅방의 읽지 않은 메시지 개수 조회
     */
    long countByChatRoomIdAndIsRead(
            Long chatRoomId,
            String isRead
    );

    /**
     * 특정 채팅방에서 특정 발신자 유형이 보낸
     * 읽지 않은 메시지 개수 조회
     */
    long countByChatRoomIdAndSenderTypeAndIsRead(
            Long chatRoomId,
            String senderType,
            String isRead
    );

    /**
     * 특정 기간에 생성된 채팅 메시지 조회
     */
    List<ChatMessage>
            findByCreatedAtBetweenOrderByCreatedAtAsc(
                    LocalDateTime startDate,
                    LocalDateTime endDate
            );

    /**
     * 특정 채팅방의 메시지 존재 여부 확인
     */
    boolean existsByChatRoomId(Long chatRoomId);

    /**
     * 특정 채팅방의 메시지 전체 삭제
     *
     * 실제 운영 환경에서는 상담 이력을 보존해야 할 수 있으므로
     * 삭제 정책을 확인한 뒤 사용하는 것이 좋다.
     */
    void deleteByChatRoomId(Long chatRoomId);

    /// 채팅방 아이디 아이디별 오름차순 정렬
    List<ChatMessage> findByChatRoomIdOrderByIdAsc(Long chatRoomId);
}