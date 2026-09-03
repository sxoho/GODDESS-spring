package dev.jpa.team1.product.productcolor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {
    List<ProductColor> findByProductIdOrderBySortOrderAsc(Long productId);
    List<ProductColor> findByProductIdAndVisibleOrderBySortOrderAsc(Long productId,String visible);
    List<ProductColor> findByProductIdAndAiApplyEnabledOrderBySortOrderAsc(Long productId,String aiApplyEnabled);
    Optional<ProductColor> findFirstByProductIdAndIsMainOrderBySortOrderAsc(Long productId,String isMain);
    boolean existsByProductIdAndColorMasterId(Long productId,Long colorId);
    void deleteByProductId(Long productId);
}
