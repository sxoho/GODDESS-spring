package dev.jpa.team1.recommend.stylerecommendation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jpa.team1.recommend.stylerecommendation.StyleRecommendation;

/**
 * 스타일 추천 Repository
 *
 * STYLE_RECOMMENDATION 테이블의
 * 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 회원이 입력한 상황과 분위기를 바탕으로
 * AI가 생성한 스타일 추천 내용을 관리한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST http://localhost:포트번호/api/style-recommendations
 * GET  http://localhost:포트번호/api/style-recommendations/{recommendationId}
 * GET  http://localhost:포트번호/api/members/{memberId}/style-recommendations
 */
public interface StyleRecommendationRepository
        extends JpaRepository<StyleRecommendation, Long> {

    /**
     * 특정 회원의 전체 스타일 추천 기록 조회
     *
     * 최근 추천부터 조회한다.
     */
    List<StyleRecommendation>
            findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 가장 최근 스타일 추천 한 건 조회
     */
    Optional<StyleRecommendation>
            findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 상황별 스타일 추천 기록 조회
     *
     * occasion 예시:
     * 면접, 데이트, 여행, 출근, 결혼식
     */
    List<StyleRecommendation>
            findByMemberIdAndOccasionOrderByCreatedAtDesc(
                    Long memberId,
                    String occasion
            );

    /**
     * 특정 회원의 분위기별 스타일 추천 기록 조회
     *
     * mood 예시:
     * 캐주얼, 단정한, 러블리, 시크, 스트릿
     */
    List<StyleRecommendation>
            findByMemberIdAndMoodOrderByCreatedAtDesc(
                    Long memberId,
                    String mood
            );

    /**
     * 특정 회원의 상황과 분위기가 모두 일치하는
     * 스타일 추천 기록 조회
     */
    List<StyleRecommendation>
            findByMemberIdAndOccasionAndMoodOrderByCreatedAtDesc(
                    Long memberId,
                    String occasion,
                    String mood
            );

    /**
     * 전체 스타일 추천 기록을 상황별로 조회
     */
    List<StyleRecommendation>
            findByOccasionOrderByCreatedAtDesc(String occasion);

    /**
     * 전체 스타일 추천 기록을 분위기별로 조회
     */
    List<StyleRecommendation>
            findByMoodOrderByCreatedAtDesc(String mood);

    /**
     * 상황에 검색어가 포함된 추천 기록 조회
     *
     * 대소문자를 구분하지 않는다.
     */
    List<StyleRecommendation>
            findByOccasionContainingIgnoreCaseOrderByCreatedAtDesc(
                    String occasion
            );

    /**
     * 분위기에 검색어가 포함된 추천 기록 조회
     *
     * 대소문자를 구분하지 않는다.
     */
    List<StyleRecommendation>
            findByMoodContainingIgnoreCaseOrderByCreatedAtDesc(
                    String mood
            );

    /**
     * 특정 기간에 생성된 스타일 추천 기록 조회
     */
    List<StyleRecommendation>
            findByCreatedAtBetweenOrderByCreatedAtDesc(
                    LocalDateTime startDate,
                    LocalDateTime endDate
            );

    /**
     * 특정 회원의 스타일 추천 기록 존재 여부 확인
     */
    boolean existsByMemberId(Long memberId);

    /**
     * 특정 회원의 스타일 추천 기록 개수 조회
     */
    long countByMemberId(Long memberId);

    /**
     * 특정 회원의 스타일 추천 기록 전체 삭제
     *
     * 회원 탈퇴 처리 등에 사용할 수 있다.
     */
    void deleteByMemberId(Long memberId);
}