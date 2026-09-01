package dev.jpa.team1.fitting.coordfitting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 코디 피팅 요청 Repository
 *
 * COORD_FITTING 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 상의, 하의, 아우터 등을 조합하여 진행하는
 * 코디 피팅 요청 정보를 관리한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST http://localhost:포트번호/api/coord-fittings
 * GET  http://localhost:포트번호/api/coord-fittings/{coordFittingId}
 * GET  http://localhost:포트번호/api/members/{memberId}/coord-fittings
 */
public interface CoordFittingRepository
        extends JpaRepository<CoordFitting, Long> {

    /**
     * 특정 회원의 코디 피팅 요청 목록 조회
     *
     * 최근 요청부터 조회한다.
     */
    List<CoordFitting> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 상태별 코디 피팅 요청 조회
     *
     * status 예시:
     * WAITING, PROCESSING, SUCCESS, FAILED
     */
    List<CoordFitting>
            findByMemberIdAndStatusOrderByCreatedAtDesc(
                    Long memberId,
                    String status
            );

    /**
     * 코디 피팅 방식별 요청 조회
     *
     * fittingMethod 예시:
     * PHOTO, PROFILE
     */
    List<CoordFitting>
            findByFittingMethodOrderByCreatedAtDesc(String fittingMethod);

    /**
     * 특정 회원의 코디 피팅 방식별 요청 조회
     */
    List<CoordFitting>
            findByMemberIdAndFittingMethodOrderByCreatedAtDesc(
                    Long memberId,
                    String fittingMethod
            );

    /**
     * 특정 회원 사진을 사용한 코디 피팅 요청 조회
     */
    List<CoordFitting>
            findByMemberPhotoIdOrderByCreatedAtDesc(Long memberPhotoId);

    /**
     * 특정 피팅 프로필을 사용한 코디 피팅 요청 조회
     */
    List<CoordFitting>
            findByFittingProfileIdOrderByCreatedAtDesc(
                    Long fittingProfileId
            );

    /**
     * 전체 코디 피팅 요청을 상태별로 조회
     */
    List<CoordFitting> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 특정 회원의 가장 최근 코디 피팅 요청 한 건 조회
     */
    Optional<CoordFitting>
            findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 기간에 생성된 코디 피팅 요청 조회
     */
    List<CoordFitting> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * 특정 회원의 코디 피팅 요청 존재 여부 확인
     */
    boolean existsByMemberId(Long memberId);
}