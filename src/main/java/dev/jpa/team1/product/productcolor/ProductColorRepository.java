package dev.jpa.team1.product.productcolor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 상품 색상 Repository
 *
 * PRODUCT_COLOR 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * Repository는 브라우저에서 직접 실행하는 URL이 없다.
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/products/{productId}/colors
 */
public interface ProductColorRepository
        extends JpaRepository<ProductColor, Long> {

    /**
     * 특정 상품의 전체 색상 목록 조회
     *
     * sortOrder 오름차순으로 정렬한다.
     */
    List<ProductColor> findByProductIdOrderBySortOrderAsc(Long productId);

    /**
     * 특정 상품의 공개 색상 목록 조회
     *
     * 예:
     * visible = "Y"
     */
    List<ProductColor> findByProductIdAndVisibleOrderBySortOrderAsc(
            Long productId,
            String visible
    );

    /**
     * 특정 상품에서 가상 피팅이 가능한 색상 목록 조회
     *
     * 예:
     * fittingEnabled = "Y"
     */
    List<ProductColor> findByProductIdAndFittingEnabledOrderBySortOrderAsc(
            Long productId,
            String fittingEnabled
    );

    /**
     * 특정 상품에서 공개 중이며 가상 피팅 가능한 색상 조회
     */
    List<ProductColor>
            findByProductIdAndVisibleAndFittingEnabledOrderBySortOrderAsc(
                    Long productId,
                    String visible,
                    String fittingEnabled
            );

    /**
     * 특정 상품의 대표 색상 조회
     *
     * 예:
     * isMain = "Y"
     */
    Optional<ProductColor> findFirstByProductIdAndIsMainOrderBySortOrderAsc(
            Long productId,
            String isMain
    );

    /**
     * 특정 상품의 공개 중인 대표 색상 조회
     */
    Optional<ProductColor>
            findFirstByProductIdAndIsMainAndVisibleOrderBySortOrderAsc(
                    Long productId,
                    String isMain,
                    String visible
            );

    /**
     * 특정 상품에 같은 색상명이 존재하는지 확인
     */
    boolean existsByProductIdAndProductColorInfoId(
            Long productId,
            Long colorId
    );

    /**
     * 특정 상품의 색상을 모두 삭제
     *
     * 상품 삭제 시 연관 색상 정리에 사용할 수 있다.
     */
    void deleteByProductId(Long productId);
}