package dev.jpa.team1.fitting.fittingsave;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 피팅 결과 저장 Repository
 *
 * FITTING_SAVE 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 사용자가 간편 피팅 또는 코디 피팅 결과를
 * '내 피팅함'에 저장할 때 사용한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST   http://localhost:포트번호/api/fitting-saves
 * GET    http://localhost:포트번호/api/members/{memberId}/fitting-saves
 * GET    http://localhost:포트번호/api/fitting-saves/{fittingSaveId}
 * DELETE http://localhost:포트번호/api/fitting-saves/{fittingSaveId}
 */
public interface FittingSaveRepository
        extends JpaRepository<FittingSave, Long> {

    /**
     * 특정 회원이 저장한 전체 피팅 결과 조회
     *
     * 최근 저장한 순서대로 조회한다.
     */
    List<FittingSave> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 피팅 유형별 저장 목록 조회
     *
     * fittingType 예시:
     * QUICK, COORD
     */
    List<FittingSave>
            findByMemberIdAndFittingTypeOrderByCreatedAtDesc(
                    Long memberId,
                    String fittingType
            );

    /**
     * 특정 간편 피팅 결과를 저장한 정보 조회
     */
    Optional<FittingSave>
            findByMemberIdAndQuickFittingResultId(
                    Long memberId,
                    Long quickFittingResultId
            );

    /**
     * 특정 코디 피팅 결과를 저장한 정보 조회
     */
    Optional<FittingSave>
            findByMemberIdAndCoordFittingResultId(
                    Long memberId,
                    Long coordFittingResultId
            );

    /**
     * 특정 회원이 간편 피팅 결과를
     * 이미 저장했는지 확인
     */
    boolean existsByMemberIdAndQuickFittingResultId(
            Long memberId,
            Long quickFittingResultId
    );

    /**
     * 특정 회원이 코디 피팅 결과를
     * 이미 저장했는지 확인
     */
    boolean existsByMemberIdAndCoordFittingResultId(
            Long memberId,
            Long coordFittingResultId
    );

    /**
     * 저장 이름에 검색어가 포함된 피팅 결과 조회
     *
     * 대소문자를 구분하지 않는다.
     */
    List<FittingSave>
            findByMemberIdAndSaveNameContainingIgnoreCaseOrderByCreatedAtDesc(
                    Long memberId,
                    String saveName
            );

    /**
     * 특정 회원의 저장된 피팅 결과 개수 조회
     */
    long countByMemberId(Long memberId);

    /**
     * 특정 간편 피팅 결과와 연결된 저장 정보 조회
     */
    List<FittingSave>
            findByQuickFittingResultId(Long quickFittingResultId);

    /**
     * 특정 코디 피팅 결과와 연결된 저장 정보 조회
     */
    List<FittingSave>
            findByCoordFittingResultId(Long coordFittingResultId);

    /**
     * 특정 회원의 저장된 피팅 결과를 모두 삭제
     *
     * 회원 탈퇴 처리 등에 사용할 수 있다.
     */
    void deleteByMemberId(Long memberId);

    List<FittingSave> findAllByMemberIdOrderByIdDesc(Long memberId);
}