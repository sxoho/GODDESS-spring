package dev.jpa.team1.chat.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 채팅방 Repository
 *
 * CHAT_ROOM 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 회원과 상담원의 실시간 상담방 또는
 * 챗봇 상담방 정보를 관리한다.
 *
 * 채팅방은 필요에 따라 상품 또는 주문 정보와
 * 연결될 수 있다.
 *
 */
public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    /**
     * 특정 회원의 전체 채팅방 목록 조회
     *
     * 최근 생성된 채팅방부터 조회한다.
     */
    List<ChatRoom> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 상태별 채팅방 조회
     *
     * status 예시:
     * WAITING, CHATTING, CLOSED
     */
    List<ChatRoom> findByMemberIdAndStatusOrderByCreatedAtDesc(
            Long memberId,
            String status
    );

    /**
     * 특정 회원의 채팅 유형별 채팅방 조회
     *
     * chatType 예시:
     * CHATBOT, COUNSELING
     */
    List<ChatRoom> findByMemberIdAndChatTypeOrderByCreatedAtDesc(
            Long memberId,
            String chatType
    );

    /**
     * 특정 회원의 채팅 유형과 상태가 모두 일치하는
     * 채팅방 목록 조회
     */
    List<ChatRoom>
            findByMemberIdAndChatTypeAndStatusOrderByCreatedAtDesc(
                    Long memberId,
                    String chatType,
                    String status
            );

    /**
     * 특정 상담원이 담당한 채팅방 목록 조회
     *
     * 최근 생성된 채팅방부터 조회한다.
     */
    List<ChatRoom> findByCounselorIdOrderByCreatedAtDesc(
            Long counselorId
    );

    /**
     * 특정 상담원이 담당한 상태별 채팅방 조회
     */
    List<ChatRoom>
            findByCounselorIdAndStatusOrderByCreatedAtDesc(
                    Long counselorId,
                    String status
            );

    /**
     * 상담원이 아직 배정되지 않은 채팅방 조회
     *
     * 상담 대기 목록에 사용할 수 있다.
     */
    List<ChatRoom>
            findByCounselorIsNullOrderByCreatedAtAsc();

    /**
     * 상담원이 아직 배정되지 않았고
     * 특정 상태인 채팅방 조회
     */
    List<ChatRoom>
            findByCounselorIsNullAndStatusOrderByCreatedAtAsc(
                    String status
            );

    /**
     * 특정 상품과 연결된 채팅방 조회
     */
    List<ChatRoom> findByProductIdOrderByCreatedAtDesc(
            Long productId
    );

    /**
     * 특정 주문과 연결된 채팅방 조회
     *
     * ChatRoom Entity의 주문 필드명은 orders이다.
     */
    List<ChatRoom> findByOrdersIdOrderByCreatedAtDesc(
            Long orderId
    );

    /**
     * 특정 회원과 상품에 해당하는 채팅방 조회
     */
    List<ChatRoom>
            findByMemberIdAndProductIdOrderByCreatedAtDesc(
                    Long memberId,
                    Long productId
            );

    /**
     * 특정 회원과 주문에 해당하는 채팅방 조회
     */
    List<ChatRoom>
            findByMemberIdAndOrdersIdOrderByCreatedAtDesc(
                    Long memberId,
                    Long orderId
            );

    /**
     * 상태별 전체 채팅방 조회
     */
    List<ChatRoom> findByStatusOrderByCreatedAtAsc(
            String status
    );

    /**
     * 채팅 유형별 전체 채팅방 조회
     */
    List<ChatRoom> findByChatTypeOrderByCreatedAtDesc(
            String chatType
    );

    /**
     * 특정 회원의 가장 최근 채팅방 한 건 조회
     */
    Optional<ChatRoom>
            findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 종료되지 않은 최근 채팅방 조회
     *
     * status 값에는 현재 사용 중인 상태를 전달한다.
     * 예: WAITING 또는 CHATTING
     */
    Optional<ChatRoom>
            findFirstByMemberIdAndStatusOrderByCreatedAtDesc(
                    Long memberId,
                    String status
            );

    /**
     * 특정 기간에 생성된 채팅방 조회
     */
    List<ChatRoom>
            findByCreatedAtBetweenOrderByCreatedAtDesc(
                    LocalDateTime startDate,
                    LocalDateTime endDate
            );

    /**
     * 특정 기간에 종료된 채팅방 조회
     */
    List<ChatRoom>
            findByClosedDateBetweenOrderByClosedDateDesc(
                    LocalDateTime startDate,
                    LocalDateTime endDate
            );

    /**
     * 특정 회원의 채팅방 개수 조회
     */
    long countByMemberId(Long memberId);

    /**
     * 특정 상태의 채팅방 개수 조회
     */
    long countByStatus(String status);

    List<ChatRoom> findByMemberIdOrderByIdDesc(Long id);
}