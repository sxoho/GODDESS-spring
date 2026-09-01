package dev.jpa.team1.product.productcolorimage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 상품 색상 이미지 Repository
 *
 * PRODUCT_COLOR_IMAGE 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * Repository는 브라우저에서 직접 실행하는 URL이 없다.
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/product-colors/{productColorId}/images
 */
public interface ProductColorImageRepository
        extends JpaRepository<ProductColorImage, Long> {

    /**
     * 특정 상품 색상의 전체 이미지 조회
     *
     * sortOrder 오름차순으로 정렬한다.
     */
    List<ProductColorImage> findByProductColorIdOrderBySortOrderAsc(
            Long productColorId
    );

    /**
     * 특정 상품 색상의 대표 이미지 조회
     *
     * 예:
     * isMain = "Y"
     */
    Optional<ProductColorImage>
            findFirstByProductColorIdAndIsMainOrderBySortOrderAsc(
                    Long productColorId,
                    String isMain
            );

    /**
     * 특정 상품 색상의 이미지 유형별 조회
     *
     * 예:
     * FRONT, BACK, DETAIL, FITTING
     */
    List<ProductColorImage>
            findByProductColorIdAndImageTypeOrderBySortOrderAsc(
                    Long productColorId,
                    String imageType
            );

    /**
     * 특정 상품 색상에서 같은 저장 파일명이 존재하는지 확인
     */
    boolean existsByProductColorIdAndFileSaved(
            Long productColorId,
            String fileSaved
    );

    /**
     * 저장 파일명으로 이미지 한 건 조회
     */
    Optional<ProductColorImage> findByFileSaved(String fileSaved);

    /**
     * 특정 상품 색상의 이미지를 모두 삭제
     *
     * 색상 삭제 시 연관 이미지 정리에 사용할 수 있다.
     */
    void deleteByProductColorId(Long productColorId);
}