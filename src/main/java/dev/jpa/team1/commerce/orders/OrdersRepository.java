package dev.jpa.team1.commerce.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 주문 Repository
 *
 * ORDERS 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 회원의 주문 기본 정보와 상품 금액, 배송비,
 * 할인 금액, 최종 결제 금액, 주문 상태를 관리한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST http://localhost:포트번호/api/orders
 * GET  http://localhost:포트번호/api/orders/{orderId}
 * GET  http://localhost:포트번호/api/members/{memberId}/orders
 */
public interface OrdersRepository
        extends JpaRepository<Orders, Long> {

    /**
     * 특정 회원의 전체 주문 목록 조회
     *
     * 최근 주문부터 조회한다.
     */
    List<Orders> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 주문 상태별 목록 조회
     *
     * status 예시:
     * PAYMENT_WAITING, PAID, PREPARING,
     * SHIPPING, COMPLETED, CANCELLED
     */
    List<Orders> findByMemberIdAndStatusOrderByCreatedAtDesc(
            Long memberId,
            String status
    );

    /**
     * 전체 주문을 상태별로 조회
     */
    List<Orders> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 특정 회원의 가장 최근 주문 한 건 조회
     */
    Optional<Orders> findFirstByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );

    /**
     * 주문명에 검색어가 포함된 주문 조회
     *
     * 대소문자를 구분하지 않는다.
     */
    List<Orders> findByOrderNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String orderName
    );

    /**
     * 특정 기간에 생성된 주문 조회
     */
    List<Orders> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * 특정 회원이 특정 기간에 주문한 목록 조회
     */
    List<Orders>
            findByMemberIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                    Long memberId,
                    LocalDateTime startDate,
                    LocalDateTime endDate
            );

    /**
     * 특정 회원의 주문 존재 여부 확인
     */
    boolean existsByMemberId(Long memberId);

    /**
     * 특정 회원의 주문 개수 조회
     */
    long countByMemberId(Long memberId);

    /**
     * 특정 상태의 전체 주문 개수 조회
     */
    long countByStatus(String status);
}