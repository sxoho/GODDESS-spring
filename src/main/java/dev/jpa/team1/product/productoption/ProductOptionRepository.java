package dev.jpa.team1.product.productoption;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 상품 옵션 Repository
 *
 * 상품, 색상, 사이즈의 조합과 재고를 관리한다.
 *
 * PRODUCT_OPTION 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * Repository는 브라우저에서 직접 실행하는 URL이 없다.
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/products/{productId}/options
 */
public interface ProductOptionRepository
        extends JpaRepository<ProductOption, Long> {

    /**
     * 특정 상품의 전체 옵션 목록 조회
     */
    List<ProductOption> findByProductIdOrderByIdAsc(Long productId);

    /**
     * 특정 상품의 공개 옵션 목록 조회
     *
     * 예:
     * visible = "Y"
     */
    List<ProductOption> findByProductIdAndVisibleOrderByIdAsc(
            Long productId,
            String visible
    );

    /**
     * 특정 상품 색상에 해당하는 옵션 목록 조회
     */
    List<ProductOption> findByProductColorIdOrderByIdAsc(
            Long productColorId
    );

    /**
     * 특정 상품 사이즈에 해당하는 옵션 목록 조회
     */
    List<ProductOption> findByProductSizeIdOrderByIdAsc(
            Long productSizeId
    );

    /**
     * 상품, 색상, 사이즈 조합으로 옵션 한 건 조회
     */
    Optional<ProductOption>
            findByProductIdAndProductColorIdAndProductSizeId(
                    Long productId,
                    Long productColorId,
                    Long productSizeId
            );

    /**
     * 같은 상품, 색상, 사이즈 조합이 이미 존재하는지 확인
     */
    boolean existsByProductIdAndProductColorIdAndProductSizeId(
            Long productId,
            Long productColorId,
            Long productSizeId
    );

    /**
     * 특정 상품의 재고가 남아 있는 옵션 목록 조회
     *
     * stock이 0보다 큰 옵션만 조회한다.
     */
    List<ProductOption> findByProductIdAndStockGreaterThanOrderByIdAsc(
            Long productId,
            Integer stock
    );

    /**
     * 특정 상품에서 공개 중이고 재고가 남아 있는 옵션 조회
     *
     * 호출 예:
     * visible = "Y"
     * stock = 0
     *
     * stock이 0보다 큰 데이터가 조회된다.
     */
    List<ProductOption>
            findByProductIdAndVisibleAndStockGreaterThanOrderByIdAsc(
                    Long productId,
                    String visible,
                    Integer stock
            );

    /**
     * 특정 상품의 옵션을 모두 삭제
     *
     * 상품 삭제 시 연관 옵션 정리에 사용할 수 있다.
     */
    void deleteByProductId(Long productId);


    void deleteByProductColorId(Long productColorId);

    void deleteByProductSizeId(Long productSizeId);

}
