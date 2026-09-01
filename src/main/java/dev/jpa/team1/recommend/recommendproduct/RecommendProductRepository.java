package dev.jpa.team1.recommend.recommendproduct;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jpa.team1.recommend.recommendproduct.RecommendProduct;

/**
 * 추천 상품 Repository
 *
 * RECOMMEND_PRODUCT 테이블의
 * 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 하나의 스타일 추천 결과에 포함된
 * 실제 상품 색상과 추천 이유를 관리한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/api/style-recommendations/{recommendationId}/products
 * GET http://localhost:포트번호/api/recommend-products/{recommendProductId}
 */
public interface RecommendProductRepository
        extends JpaRepository<RecommendProduct, Long> {

    /**
     * 특정 스타일 추천에 포함된 전체 추천 상품 조회
     *
     * sortOrder 오름차순으로 조회한다.
     */
    List<RecommendProduct>
            findByStyleRecommendationIdOrderBySortOrderAsc(
                    Long styleRecommendationId
            );

    /**
     * 특정 스타일 추천에서 상품 종류별 추천 상품 조회
     *
     * itemType 예시:
     * TOP, BOTTOM, OUTER, GLASSES
     */
    List<RecommendProduct>
            findByStyleRecommendationIdAndItemTypeOrderBySortOrderAsc(
                    Long styleRecommendationId,
                    String itemType
            );

    /**
     * 특정 스타일 추천에서 특정 상품 색상을 조회
     */
    Optional<RecommendProduct>
            findByStyleRecommendationIdAndProductColorId(
                    Long styleRecommendationId,
                    Long productColorId
            );

    /**
     * 특정 스타일 추천에 같은 상품 색상이
     * 이미 포함되어 있는지 확인
     */
    boolean existsByStyleRecommendationIdAndProductColorId(
            Long styleRecommendationId,
            Long productColorId
    );

    /**
     * 특정 상품 색상이 추천된 전체 기록 조회
     *
     * 추천 순서대로 조회한다.
     */
    List<RecommendProduct>
            findByProductColorIdOrderBySortOrderAsc(Long productColorId);

    /**
     * 상품 종류별 전체 추천 상품 조회
     */
    List<RecommendProduct>
            findByItemTypeOrderBySortOrderAsc(String itemType);

    /**
     * 특정 스타일 추천의 추천 상품 개수 조회
     */
    long countByStyleRecommendationId(Long styleRecommendationId);

    /**
     * 특정 스타일 추천과 상품 종류에 해당하는
     * 추천 상품 개수 조회
     */
    long countByStyleRecommendationIdAndItemType(
            Long styleRecommendationId,
            String itemType
    );

    /**
     * 특정 스타일 추천의 첫 번째 추천 상품 조회
     *
     * sortOrder가 가장 작은 상품 한 건을 가져온다.
     */
    Optional<RecommendProduct>
            findFirstByStyleRecommendationIdOrderBySortOrderAsc(
                    Long styleRecommendationId
            );

    /**
     * 특정 스타일 추천에서 상품 종류별
     * 첫 번째 추천 상품 조회
     */
    Optional<RecommendProduct>
            findFirstByStyleRecommendationIdAndItemTypeOrderBySortOrderAsc(
                    Long styleRecommendationId,
                    String itemType
            );

    /**
     * 특정 스타일 추천에 포함된 추천 상품 전체 삭제
     *
     * 추천 결과를 다시 생성하거나 삭제할 때 사용할 수 있다.
     */
    void deleteByStyleRecommendationId(Long styleRecommendationId);

    /**
     * 특정 상품 색상과 연결된 추천 상품 전체 삭제
     *
     * 상품 색상 삭제 시 연관 추천 데이터 정리에 사용할 수 있다.
     */
    void deleteByProductColorId(Long productColorId);
}