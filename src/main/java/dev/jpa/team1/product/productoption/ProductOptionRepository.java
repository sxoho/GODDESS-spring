package dev.jpa.team1.product.productoption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ProductOptionRepository extends JpaRepository<ProductOption,Long>{
 List<ProductOption> findByProductIdOrderByIdAsc(Long productId);
 List<ProductOption> findByProductIdAndVisibleAndStockGreaterThanOrderByIdAsc(Long productId,String visible,Integer stock);
 boolean existsByProductIdAndProductColorIdAndProductVariantId(Long productId,Long productColorId,Long productVariantId);
 void deleteByProductId(Long productId); void deleteByProductColorId(Long productColorId); void deleteByProductVariantId(Long productVariantId);
}
