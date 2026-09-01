package dev.jpa.team1.fitting.quickfittingresult;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 간편 피팅 결과 Repository
 *
 * QUICK_FITTING_RESULT 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 하나의 간편 피팅 요청에는 상품 색상별로
 * 여러 개의 결과가 만들어질 수 있다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/api/quick-fittings/{quickFittingId}/results
 * GET http://localhost:포트번호/api/quick-fitting-results/{resultId}
 */
public interface QuickFittingResultRepository
        extends JpaRepository<QuickFittingResult, Long> {

    /**
     * 특정 간편 피팅 요청의 전체 결과 조회
     *
     * 생성 순서대로 조회한다.
     */
    List<QuickFittingResult>
            findByQuickFittingIdOrderByCreatedAtAsc(Long quickFittingId);

    /**
     * 특정 간편 피팅 요청의 성공 또는 실패 결과 조회
     *
     * success 예시:
     * Y = 성공
     * N = 실패
     */
    List<QuickFittingResult>
            findByQuickFittingIdAndSuccessOrderByCreatedAtAsc(
                    Long quickFittingId,
                    String success
            );

    /**
     * 특정 간편 피팅 요청과 상품 색상에 해당하는
     * 결과 한 건 조회
     */
    Optional<QuickFittingResult>
            findByQuickFittingIdAndProductColorId(
                    Long quickFittingId,
                    Long productColorId
            );

    /**
     * 특정 간편 피팅 요청과 상품 색상의
     * 결과가 이미 존재하는지 확인
     */
    boolean existsByQuickFittingIdAndProductColorId(
            Long quickFittingId,
            Long productColorId
    );

    /**
     * 특정 상품 색상을 사용한 피팅 결과 조회
     */
    List<QuickFittingResult>
            findByProductColorIdOrderByCreatedAtDesc(Long productColorId);

    /**
     * 성공 여부에 따른 전체 간편 피팅 결과 조회
     */
    List<QuickFittingResult>
            findBySuccessOrderByCreatedAtDesc(String success);

    /**
     * 저장된 파일명으로 결과 조회
     */
    Optional<QuickFittingResult> findByFileSaved(String fileSaved);

    /**
     * 특정 간편 피팅 요청에 결과가 하나라도 존재하는지 확인
     */
    boolean existsByQuickFittingId(Long quickFittingId);

    /**
     * 특정 간편 피팅 요청의 결과 개수 조회
     */
    long countByQuickFittingId(Long quickFittingId);

    /**
     * 특정 간편 피팅 요청의 결과를 모두 삭제
     */
    void deleteByQuickFittingId(Long quickFittingId);
}