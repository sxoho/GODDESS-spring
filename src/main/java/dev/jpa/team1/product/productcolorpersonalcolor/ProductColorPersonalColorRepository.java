package dev.jpa.team1.product.productcolorpersonalcolor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductColorPersonalColorRepository extends JpaRepository<ProductColorPersonalColor, Long> {
    List<ProductColorPersonalColor> findByProductColorIdOrderByIdAsc(Long productColorId);
    void deleteByProductColorId(Long productColorId);
}
