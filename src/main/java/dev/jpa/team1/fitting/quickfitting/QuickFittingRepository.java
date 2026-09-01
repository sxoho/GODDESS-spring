package dev.jpa.team1.fitting.quickfitting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 간편 피팅 요청 Repository
 *
 * QUICK_FITTING 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST http://localhost:포트번호/api/quick-fittings
 * GET  http://localhost:포트번호/api/quick-fittings/{quickFittingId}
 * GET  http://localhost:포트번호/api/members/{memberId}/quick-fittings
 */
public interface QuickFittingRepository
        extends JpaRepository<QuickFitting, Long> {

    /**
     * 특정 회원의 간편 피팅 요청 목록 조회
     *
     * 최근 요청부터 조회한다.
     */
    List<QuickFitting> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 간편 피팅 상태별 목록 조회
     *
     * status 예시:
     * WAITING, PROCESSING, SUCCESS, FAILED
     */
    List<QuickFitting> findByMemberIdAndStatusOrderByCreatedAtDesc(
            Long memberId,
            String status
    );

    /**
     * 특정 상품에 대한 간편 피팅 요청 목록 조회
     */
    List<QuickFitting> findByProductIdOrderByCreatedAtDesc(Long productId);

    /**
     * 특정 회원이 특정 상품에 요청한 간편 피팅 목록 조회
     */
    List<QuickFitting>
            findByMemberIdAndProductIdOrderByCreatedAtDesc(
                    Long memberId,
                    Long productId
            );

    /**
     * 특정 회원 사진으로 요청한 간편 피팅 목록 조회
     */
    List<QuickFitting>
            findByMemberPhotoIdOrderByCreatedAtDesc(Long memberPhotoId);

    /**
     * 회원, 회원 사진, 상품이 모두 같은
     * 가장 최근 간편 피팅 요청 한 건 조회
     *
     * 동일한 조건의 기존 요청이나 결과가 있는지
     * 확인할 때 사용할 수 있다.
     */
    Optional<QuickFitting>
            findFirstByMemberIdAndMemberPhotoIdAndProductIdOrderByCreatedAtDesc(
                    Long memberId,
                    Long memberPhotoId,
                    Long productId
            );

    /**
     * 전체 간편 피팅 요청을 상태별로 조회
     */
    List<QuickFitting> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 특정 기간에 생성된 간편 피팅 요청 조회
     */
    List<QuickFitting> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * 특정 회원의 가장 최근 간편 피팅 요청 한 건 조회
     */
    Optional<QuickFitting>
            findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 간편 피팅 요청 존재 여부 확인
     */
    boolean existsByMemberId(Long memberId);
}