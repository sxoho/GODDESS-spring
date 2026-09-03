package dev.jpa.team1.product.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 상품 Repository
 *
 * PRODUCT 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * 상품 이름으로 정확히 일치하는 상품 한 건 조회
     */
    Optional<Product> findByName(String name);

    /**
     * 동일한 상품 이름이 존재하는지 확인
     */
    boolean existsByName(String name);

    /**
     * 특정 카테고리에 속한 상품 목록 조회
     * 최신 등록 상품부터 조회
     */
    List<Product> findByCategoryIdOrderByCreatedAtDesc(Long categoryId);

    /**
     * 특정 카테고리에 속하면서 특정 상태인 상품 목록 조회
     */
    List<Product> findByCategoryIdAndStatusOrderByCreatedAtDesc(
            Long categoryId,
            String status
    );

    /**
     * 상품 상태별 목록 조회
     */
    List<Product> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 상품명에 검색어가 포함된 상품 목록 조회
     * 대소문자를 구분하지 않음
     */
    List<Product> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String name
    );

    /**
     * 특정 상태이면서 상품명에 검색어가 포함된 상품 조회
     */
    List<Product> findByStatusAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String status,
            String name
    );

    /**
     * AI 뷰티 적용이 가능한 상품 목록 조회
     */
    List<Product> findByAiApplyEnabledOrderByCreatedAtDesc(
            String aiApplyEnabled
    );

    /**
     * 특정 카테고리에 속하면서 AI 뷰티 적용이 가능한 상품 조회
     */
    List<Product> findByCategoryIdAndAiApplyEnabledOrderByCreatedAtDesc(
            Long categoryId,
            String aiApplyEnabled
    );

    /**
     * 특정 가격 이하의 상품 목록 조회
     * 가격이 낮은 순서
     */
    List<Product> findByPriceLessThanEqualOrderByPriceAsc(
            Integer price
    );

    /**
     * 최소 가격과 최대 가격 사이 상품 조회
     */
    List<Product> findByPriceBetweenOrderByPriceAsc(
            Integer minPrice,
            Integer maxPrice
    );

    /**
     * 상품 조회수가 높은 순서
     */
    List<Product> findAllByOrderByViewCountDesc();

    /**
     * 전체 상품 최신 등록순
     */
    List<Product> findAllByOrderByCreatedAtDesc();

    /**
     * 특정 카테고리 상품 가격 낮은순
     */
    List<Product> findByCategoryIdOrderByPriceAsc(
            Long categoryId
    );

    /**
     * 특정 카테고리 상품 가격 높은순
     */
    List<Product> findByCategoryIdOrderByPriceDesc(
            Long categoryId
    );


    // =========================
    // 페이징
    // =========================

    /**
     * 전체 상품 페이징
     *
     * JpaRepository에 기본 제공되므로
     * 사실 직접 선언하지 않아도 사용 가능
     */
    Page<Product> findAll(Pageable pageable);


    /**
     * WOMEN / MEN 같은 대분류와
     * 해당 대분류의 하위 카테고리 상품까지 조회
     *
     * 예:
     * 여성
     *  ├ 상의
     *  └ 하의
     */
    @Query("""
    	    SELECT p
    	    FROM Product p
    	    JOIN p.category c
    	    WHERE c.id = :categoryId
    	       OR c.parent.id = :categoryId
    	""")
    	Page<Product> findByMainCategory(
    	        @Param("categoryId") Long categoryId,
    	        Pageable pageable
    	);


    /**
     * 상의 / 하의 같은 특정 하위 카테고리 페이징
     */
    Page<Product> findByCategory_Id(
            Long categoryId,
            Pageable pageable
    );
    
    /**
     * 특정 카테고리를 사용하는 상품이 존재하는지 확인
     * 카테고리 삭제 전 사용 여부 확인용
     */
    boolean existsByCategory_Id(Long categoryId);


}
