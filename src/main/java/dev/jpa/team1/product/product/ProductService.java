package dev.jpa.team1.product.product;

import dev.jpa.team1.product.category.Category;
import dev.jpa.team1.product.category.CategoryRepository;
import dev.jpa.team1.product.search.ProductSearchKeyword;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    /**
     * 전체 상품 조회
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll() {

        return repository.findAll()
                .stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    /**
     * 사용자 상품 페이징 조회
     *
     * 판매중지 상품은 사용자 쇼핑몰에서 제외한다.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findPage(
            int page,
            int size,
            Long mainCategoryId,
            Long categoryId,
            String sort,
            String keyword
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                getSort(sort)
        );

        String normalizedKeyword =
                keyword == null ? "" : keyword.trim();

        List<String> terms =
                normalizedKeyword.isBlank()
                        ? List.of()
                        : ProductSearchKeyword
                                .buildSearchTerms(
                                        normalizedKeyword
                                );

        Specification<Product> specification =
                buildSearchSpecification(
                        mainCategoryId,
                        categoryId,
                        terms,
                        true
                );

        Page<Product> result =
                repository.findAll(
                        specification,
                        pageable
                );

        return result.map(ProductResponseDto::from);
    }

    /**
     * 관리자 상품 페이징 조회
     *
     * 판매중 / 품절 / 판매중지 상품을 모두 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAdminPage(
            int page,
            int size,
            Long mainCategoryId,
            Long categoryId,
            String sort,
            String keyword
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                getSort(sort)
        );

        String normalizedKeyword =
                keyword == null ? "" : keyword.trim();

        List<String> terms =
                normalizedKeyword.isBlank()
                        ? List.of()
                        : ProductSearchKeyword
                                .buildSearchTerms(
                                        normalizedKeyword
                                );

        Specification<Product> specification =
                buildSearchSpecification(
                        mainCategoryId,
                        categoryId,
                        terms,
                        false
                );

        Page<Product> result =
                repository.findAll(
                        specification,
                        pageable
                );

        return result.map(ProductResponseDto::from);
    }

    /**
     * 검색용 JPA Specification 생성
     *
     * 검색 대상:
     * - 상품명
     * - 브랜드
     * - 하위 카테고리명
     * - 대분류명
     *
     * categoryId / mainCategoryId 조건도 기존 페이징과 동일하게 함께 적용한다.
     */
    private Specification<Product> buildSearchSpecification(
            Long mainCategoryId,
            Long categoryId,
            List<String> searchTerms,
            boolean excludeStopped
    ) {

        return (root, query, cb) -> {

            Join<Product, Category> category =
                    root.join("category");

            Join<Category, Category> parent =
                    category.join(
                            "parent",
                            JoinType.LEFT
                    );

            List<Predicate> conditions =
                    new ArrayList<>();

            // 사용자 조회에서만 판매중지 상품을 제외한다.
            // 관리자 조회는 판매중지 상품도 그대로 보여준다.
            if (excludeStopped) {
                conditions.add(
                        cb.notEqual(
                                root.get("status"),
                                "판매중지"
                        )
                );
            }

            if (categoryId != null) {
                conditions.add(
                        cb.equal(
                                category.get("id"),
                                categoryId
                        )
                );
            }

            if (mainCategoryId != null) {
                conditions.add(
                        cb.or(
                                cb.equal(
                                        category.get("id"),
                                        mainCategoryId
                                ),
                                cb.equal(
                                        parent.get("id"),
                                        mainCategoryId
                                )
                        )
                );
            }

            List<Predicate> keywordConditions =
                    new ArrayList<>();

            for (String term : searchTerms) {

                String pattern =
                        "%" + term.toLowerCase() + "%";

                keywordConditions.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                pattern
                        )
                );

                keywordConditions.add(
                        cb.like(
                                cb.lower(root.get("brand")),
                                pattern
                        )
                );

                keywordConditions.add(
                        cb.like(
                                cb.lower(category.get("name")),
                                pattern
                        )
                );

                keywordConditions.add(
                        cb.like(
                                cb.lower(parent.get("name")),
                                pattern
                        )
                );
            }

            if (!keywordConditions.isEmpty()) {
                conditions.add(
                        cb.or(
                                keywordConditions.toArray(
                                        new Predicate[0]
                                )
                        )
                );
            }

            return cb.and(
                    conditions.toArray(
                            new Predicate[0]
                    )
            );
        };
    }

    /**
     * BEST 상품 최대 100개
     *
     * 판매중지 상품은 제외한다.
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findBest100(String keyword) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        100,
                        Sort.by(
                                Sort.Direction.DESC,
                                "viewCount"
                        )
                );

        String normalizedKeyword =
                keyword == null ? "" : keyword.trim();

        List<String> terms =
                normalizedKeyword.isBlank()
                        ? List.of()
                        : ProductSearchKeyword
                                .buildSearchTerms(
                                        normalizedKeyword
                                );

        Specification<Product> specification =
                buildSearchSpecification(
                        null,
                        null,
                        terms,
                        true
                );

        Page<Product> result =
                repository.findAll(
                        specification,
                        pageable
                );

        return result
                .getContent()
                .stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    /**
     * 정렬 설정
     */
    private Sort getSort(String sort) {

        if (sort == null) {

            return Sort.by(
                    Sort.Direction.DESC,
                    "createdAt"
            );
        }

        return switch (sort) {

            case "priceLow" ->
                    Sort.by(
                            Sort.Direction.ASC,
                            "price"
                    );

            case "priceHigh" ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "price"
                    );

            case "new" ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "createdAt"
                    );

            case "recommended" ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "createdAt"
                    );

            default ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "createdAt"
                    );
        };
    }

    /**
     * 상품 단건 조회
     */
    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {

        Product entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found. id=" + id
                                )
                        );

        return ProductResponseDto.from(entity);
    }

    /**
     * 상품 등록
     */
    @Transactional
    public ProductResponseDto save(
            ProductRequestDto requestDto
    ) {

        // 카테고리 조회
        Category category =
                categoryRepository
                        .findById(
                                requestDto.getCategoryId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Category not found. id="
                                                + requestDto.getCategoryId()
                                )
                        );

        // 상품 생성
        Product entity =
                Product.builder()
                        .category(category)
                        .brand(
                                requestDto.getBrand()
                        )
                        .name(
                                requestDto.getName()
                        )
                        .description(
                                requestDto.getDescription()
                        )
                        .price(
                                requestDto.getPrice()
                        )
                        .discount(
                                requestDto.getDiscount()
                        )
                        .material(
                                requestDto.getMaterial()
                        )
                        .washing(
                                requestDto.getWashing()
                        )
                        .fittingEnabled(
                                requestDto.getFittingEnabled()
                        )
                        .status(
                                requestDto.getStatus()
                        )
                        .build();

        Product saved =
                repository.save(entity);

        return ProductResponseDto.from(saved);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public ProductResponseDto update(
            Long id,
            ProductRequestDto requestDto
    ) {

        // 수정할 상품 조회
        Product product =
                repository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found. id=" + id
                                )
                        );

        // 변경할 카테고리 조회
        Category category =
                categoryRepository
                        .findById(
                                requestDto.getCategoryId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Category not found. id="
                                                + requestDto.getCategoryId()
                                )
                        );

        // 엔티티 값 수정
        product.update(
                category,
                requestDto.getBrand(),
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPrice(),
                requestDto.getDiscount(),
                requestDto.getMaterial(),
                requestDto.getWashing(),
                requestDto.getFittingEnabled(),
                requestDto.getStatus()
        );

        /*
         * @Transactional 안에서 조회한 엔티티이므로
         * repository.save(product)를 다시 호출하지 않아도
         * JPA Dirty Checking으로 UPDATE 된다.
         */
        return ProductResponseDto.from(product);
    }

    /**
     * 상품 삭제 처리
     *
     * 실제 DB 행 삭제가 아니라
     * 판매상태를 "판매중지"로 변경
     */
    @Transactional
    public void stopSale(Long id) {

        Product product =
                repository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found. id=" + id
                                )
                        );

        product.stopSale();
    }
    
    @Transactional
    public void resumeSale(Long id) {

        Product product =
                repository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found. id=" + id
                                )
                        );

        product.resumeSale();
    }
}