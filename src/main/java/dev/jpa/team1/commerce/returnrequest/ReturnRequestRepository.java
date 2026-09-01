package dev.jpa.team1.commerce.returnrequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 반품 요청 Repository
 *
 * RETURN_REQUEST 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 주문 상세 상품별 반품 사유, 처리 상태,
 * 첨부 파일, 처리일과 환불 금액을 관리한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST  http://localhost:포트번호/api/return-requests
 * GET   http://localhost:포트번호/api/return-requests/{returnRequestId}
 * GET   http://localhost:포트번호/api/members/{memberId}/return-requests
 * PATCH http://localhost:포트번호/api/return-requests/{returnRequestId}/status
 */
public interface ReturnRequestRepository
        extends JpaRepository<ReturnRequest, Long> {

    /**
     * 특정 회원의 전체 반품 요청 조회
     *
     * 최근 요청부터 조회한다.
     */
    List<ReturnRequest>
            findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 반품 상태별 요청 조회
     *
     * status 예시:
     * REQUESTED, APPROVED, REJECTED,
     * COLLECTING, COMPLETED
     */
    List<ReturnRequest>
            findByMemberIdAndStatusOrderByCreatedAtDesc(
                    Long memberId,
                    String status
            );

    /**
     * 특정 주문 상세에 대한 반품 요청 조회
     */
    Optional<ReturnRequest> findByOrderDetailId(Long orderDetailId);

    /**
     * 특정 주문 상세에 반품 요청이 존재하는지 확인
     */
    boolean existsByOrderDetailId(Long orderDetailId);

    /**
     * 반품 처리 상태별 전체 목록 조회
     */
    List<ReturnRequest> findByStatusOrderByCreatedAtDesc(
            String status
    );

    /**
     * 반품 사유 유형별 목록 조회
     *
     * reasonType 예시:
     * CHANGE_MIND, DEFECT, WRONG_DELIVERY, SIZE
     */
    List<ReturnRequest>
            findByReasonTypeOrderByCreatedAtDesc(String reasonType);

    /**
     * 특정 회원의 반품 사유 유형별 목록 조회
     */
    List<ReturnRequest>
            findByMemberIdAndReasonTypeOrderByCreatedAtDesc(
                    Long memberId,
                    String reasonType
            );

    /**
     * 특정 기간에 생성된 반품 요청 조회
     */
    List<ReturnRequest>
            findByCreatedAtBetweenOrderByCreatedAtDesc(
                    LocalDateTime startDate,
                    LocalDateTime endDate
            );

    /**
     * 특정 회원의 반품 요청 개수 조회
     */
    long countByMemberId(Long memberId);

    /**
     * 특정 상태의 반품 요청 개수 조회
     */
    long countByStatus(String status);

    /**
     * 특정 회원의 반품 요청 전체 삭제
     *
     * 일반적으로 실제 운영에서는 이력을 삭제하기보다
     * 상태를 변경하여 보관하는 편이 좋다.
     */
    void deleteByMemberId(Long memberId);

    /// 회원 아이디 내림차순 정렬 조회
    List<ReturnRequest> findByMemberIdOrderByIdDesc(Long memberId);
}