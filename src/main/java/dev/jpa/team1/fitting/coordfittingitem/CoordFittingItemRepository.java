package dev.jpa.team1.fitting.coordfittingitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 코디 피팅 구성 상품 Repository
 *
 * COORD_FITTING_ITEM 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 하나의 코디 피팅 요청에 포함되는 상의, 하의,
 * 아우터 등의 상품 구성 정보를 관리한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/api/coord-fittings/{coordFittingId}/items
 */
public interface CoordFittingItemRepository
        extends JpaRepository<CoordFittingItem, Long> {

    /**
     * 특정 코디 피팅 요청에 포함된 전체 상품 조회
     *
     * sortOrder 오름차순으로 조회한다.
     */
    List<CoordFittingItem>
            findByCoordFittingIdOrderBySortOrderAsc(Long coordFittingId);

    /**
     * 특정 코디 피팅 요청에서 상품 종류별 항목 조회
     *
     * itemType 예시:
     * TOP, BOTTOM, OUTER
     */
    Optional<CoordFittingItem>
            findByCoordFittingIdAndItemType(
                    Long coordFittingId,
                    String itemType
            );

    /**
     * 특정 코디 피팅 요청에서 같은 상품 종류가
     * 이미 등록되어 있는지 확인
     */
    boolean existsByCoordFittingIdAndItemType(
            Long coordFittingId,
            String itemType
    );

    /**
     * 특정 상품 색상이 포함된 코디 피팅 항목 조회
     */
    List<CoordFittingItem>
            findByProductColorIdOrderByIdDesc(Long productColorId);

    /**
     * 특정 상품 옵션이 포함된 코디 피팅 항목 조회
     */
    List<CoordFittingItem>
            findByProductOptionIdOrderByIdDesc(Long productOptionId);

    /**
     * 특정 코디 피팅 요청과 상품 색상에 해당하는 항목 조회
     */
    Optional<CoordFittingItem>
            findByCoordFittingIdAndProductColorId(
                    Long coordFittingId,
                    Long productColorId
            );

    /**
     * 특정 코디 피팅 요청과 상품 옵션에 해당하는 항목 조회
     */
    Optional<CoordFittingItem>
            findByCoordFittingIdAndProductOptionId(
                    Long coordFittingId,
                    Long productOptionId
            );

    /**
     * 특정 코디 피팅 요청의 구성 상품 개수 조회
     */
    long countByCoordFittingId(Long coordFittingId);

    /**
     * 특정 코디 피팅 요청에 포함된 상품 항목을 모두 삭제
     */
    void deleteByCoordFittingId(Long coordFittingId);
}