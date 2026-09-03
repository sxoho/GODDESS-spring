package dev.jpa.team1.recommend.recommendproduct; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface RecommendProductRepository extends JpaRepository<RecommendProduct,Long>{
 List<RecommendProduct> findByBeautyRecommendationIdOrderBySortOrderAsc(Long recommendationId); List<RecommendProduct> findByBeautyRecommendationIdAndItemTypeOrderBySortOrderAsc(Long recommendationId,String itemType); List<RecommendProduct> findByProductIdOrderBySortOrderAsc(Long productId); void deleteByBeautyRecommendationId(Long recommendationId);
}
