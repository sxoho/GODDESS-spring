package dev.jpa.team1.commerce.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 결제 Repository
 *
 * PAYMENT 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 주문별 결제 수단, 결제 금액, 결제 상태,
 * 승인 번호와 결제 취소일을 관리한다.
 *
 * Orders와 Payment는 1:1 관계이다.
 * 따라서 하나의 주문에는 하나의 결제 정보가 존재한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST http://localhost:포트번호/api/payments
 * GET  http://localhost:포트번호/api/orders/{orderId}/payment
 * POST http://localhost:포트번호/api/payments/{paymentId}/cancel
 */
public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    /**
     * 특정 주문의 결제 정보 조회
     */
    Optional<Payment> findByOrdersId(Long orderId);

    /**
     * 특정 주문에 결제 정보가 존재하는지 확인
     */
    boolean existsByOrdersId(Long orderId);

    /**
     * 결제 상태별 결제 목록 조회
     *
     * status 예시:
     * READY, PAID, FAILED, CANCELLED
     */
    List<Payment> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 결제 수단별 결제 목록 조회
     *
     * paymentMethod 예시:
     * CARD, BANK_TRANSFER, KAKAO_PAY
     */
    List<Payment> findByPaymentMethodOrderByCreatedAtDesc(
            String paymentMethod
    );

    /**
     * 결제 승인 번호로 결제 정보 조회
     */
    Optional<Payment> findByApprovalNo(String approvalNo);

    /**
     * 같은 승인 번호가 존재하는지 확인
     */
    boolean existsByApprovalNo(String approvalNo);

    /**
     * 특정 기간에 생성된 결제 목록 조회
     */
    List<Payment> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * 특정 결제 상태의 개수 조회
     */
    long countByStatus(String status);

    /**
     * 특정 주문의 결제 정보 삭제
     */
    void deleteByOrdersId(Long orderId);
}