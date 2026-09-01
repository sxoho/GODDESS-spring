package dev.jpa.team1.product.productsize;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 상품 사이즈 Repository
 *
 * PRODUCT_SIZE 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * Repository는 브라우저에서 직접 실행하는 URL이 없다.
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/products/{productId}/sizes
 */
public interface ProductSizeRepository
        extends JpaRepository<ProductSize, Long> {

    /**
     * 특정 상품의 전체 사이즈 목록 조회
     *
     * sortOrder 오름차순으로 정렬한다.
     */
    List<ProductSize> findByProductIdOrderBySortOrderAsc(Long productId);

    /**
     * 특정 상품의 공개 사이즈 목록 조회
     *
     * 예:
     * visible = "Y"
     */
    List<ProductSize> findByProductIdAndVisibleOrderBySortOrderAsc(
            Long productId,
            String visible
    );

    /**
     * 특정 상품에서 사이즈명으로 사이즈 한 건 조회
     *
     * 예:
     * S, M, L, XL
     */
    Optional<ProductSize> findByProductIdAndSizeName(
            Long productId,
            String sizeName
    );

    /**
     * 특정 상품에 동일한 사이즈명이 존재하는지 확인
     */
    boolean existsByProductIdAndSizeName(
            Long productId,
            String sizeName
    );

    /**
     * 사이즈 이름별 전체 목록 조회
     *
     * 예:
     * 모든 상품 중 sizeName이 "M"인 사이즈 조회
     */
    List<ProductSize> findBySizeNameOrderByProductIdAsc(String sizeName);

    /**
     * 특정 상품의 사이즈를 모두 삭제
     *
     * 상품 삭제 시 연관 사이즈 정리에 사용할 수 있다.
     */
    void deleteByProductId(Long productId);
}