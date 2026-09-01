package dev.jpa.team1.fitting.coordfittingresult;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 코디 피팅 결과 Repository
 *
 * COORD_FITTING_RESULT 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * CoordFitting과 CoordFittingResult는 1:1 관계이다.
 * 따라서 하나의 코디 피팅 요청에는 결과 한 건만 존재한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/api/coord-fittings/{coordFittingId}/result
 * GET http://localhost:포트번호/api/coord-fitting-results/{resultId}
 */
public interface CoordFittingResultRepository
        extends JpaRepository<CoordFittingResult, Long> {

    /**
     * 특정 코디 피팅 요청의 결과 한 건 조회
     */
    Optional<CoordFittingResult>
            findByCoordFittingId(Long coordFittingId);

    /**
     * 특정 코디 피팅 요청에 결과가 존재하는지 확인
     */
    boolean existsByCoordFittingId(Long coordFittingId);

    /**
     * 성공 여부에 따른 코디 피팅 결과 조회
     *
     * success 예시:
     * Y = 성공
     * N = 실패
     */
    List<CoordFittingResult>
            findBySuccessOrderByCreatedAtDesc(String success);

    /**
     * 저장 파일명으로 코디 피팅 결과 조회
     */
    Optional<CoordFittingResult> findByFileSaved(String fileSaved);

    /**
     * 특정 회원의 코디 피팅 결과 목록 조회
     *
     * CoordFittingResult → CoordFitting → Member → id
     * 순서로 연관관계를 따라 조회한다.
     */
    List<CoordFittingResult>
            findByCoordFittingMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 성공한 코디 피팅 결과 조회
     */
    List<CoordFittingResult>
            findByCoordFittingMemberIdAndSuccessOrderByCreatedAtDesc(
                    Long memberId,
                    String success
            );

    /**
     * 특정 코디 피팅 요청의 결과 삭제
     */
    void deleteByCoordFittingId(Long coordFittingId);
}