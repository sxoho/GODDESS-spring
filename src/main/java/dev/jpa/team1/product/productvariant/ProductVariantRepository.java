package dev.jpa.team1.product.productvariant;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByProductIdOrderBySortOrderAsc(Long productId);
    boolean existsByProductIdAndVariantName(Long productId, String variantName);
    void deleteByProductId(Long productId);
}
