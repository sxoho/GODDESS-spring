package dev.jpa.team1.product.product;

import dev.jpa.team1.product.category.Category;
import dev.jpa.team1.product.category.CategoryRepository;
import dev.jpa.team1.product.search.ProductSearchKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.*;
import java.util.*;

@Service @RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly=true) public List<ProductResponseDto> findAll(){ return repository.findAll().stream().map(ProductResponseDto::from).toList(); }

    @Transactional(readOnly=true)
    public Page<ProductResponseDto> findPage(
            int page,
            int size,
            Long mainCategoryId,
            Long categoryId,
            String sort,
            String keyword,
            String skinType,
            String personalColor,
            String aiApplyEnabled
    ) {
        return repository.findAll(
                buildSearchSpecification(
                        mainCategoryId,
                        categoryId,
                        terms(keyword),
                        true,
                        skinType,
                        personalColor,
                        aiApplyEnabled
                ),
                PageRequest.of(page, size, getSort(sort))
        ).map(ProductResponseDto::from);
    }

    @Transactional(readOnly=true)
    public Page<ProductResponseDto> findAdminPage(int page,int size,Long mainCategoryId,Long categoryId,String sort,String keyword){
        return repository.findAll(
                buildSearchSpecification(
                        mainCategoryId,
                        categoryId,
                        terms(keyword),
                        false,
                        null,
                        null,
                        null
                ),
                PageRequest.of(page,size,getSort(sort))
        ).map(ProductResponseDto::from);
    }

    @Transactional(readOnly=true)
    public List<ProductResponseDto> findBest100(String keyword){
        return repository.findAll(
                buildSearchSpecification(
                        null,
                        null,
                        terms(keyword),
                        true,
                        null,
                        null,
                        null
                ),
                PageRequest.of(0,100,Sort.by(Sort.Direction.DESC,"viewCount"))
        )
                .getContent().stream().map(ProductResponseDto::from).toList();
    }

    private List<String> terms(String keyword){
        String k=keyword==null?"":keyword.trim();
        return k.isBlank()?List.of(): ProductSearchKeyword.buildSearchTerms(k);
    }

    /**
     * 사용자 상품 목록 검색 조건.
     *
     * skinType / personalColor는 PRODUCT 컬럼에
     * "건성,복합성", "봄웜,여름쿨" 같은 comma-separated 문자열로 저장한다.
     *
     * React에서 여러 필터를 선택하면 요청도 comma-separated로 들어오므로,
     * 같은 그룹 안에서는 OR, 서로 다른 그룹끼리는 AND로 검색한다.
     *
     * 예:
     * skinType=건성,복합성
     * personalColor=봄웜
     *
     * => (skinType LIKE '%건성%' OR skinType LIKE '%복합성%')
     *    AND personalColor LIKE '%봄웜%'
     */
    private Specification<Product> buildSearchSpecification(
            Long mainCategoryId,
            Long categoryId,
            List<String> searchTerms,
            boolean publicOnly,
            String skinType,
            String personalColor,
            String aiApplyEnabled
    ) {
        return (root, query, cb) -> {
            Join<Product, Category> category = root.join("category");
            Join<Category, Category> parent = category.join("parent", JoinType.LEFT);

            List<Predicate> conditions = new ArrayList<>();

            if (publicOnly) {
                conditions.add(cb.equal(root.get("status"), "ON_SALE"));
            }

            if (categoryId != null) {
                conditions.add(cb.equal(category.get("id"), categoryId));
            }

            if (mainCategoryId != null) {
                conditions.add(
                        cb.or(
                                cb.equal(category.get("id"), mainCategoryId),
                                cb.equal(parent.get("id"), mainCategoryId)
                        )
                );
            }

            if (!searchTerms.isEmpty()) {
                List<Predicate> keywordConditions = new ArrayList<>();

                for (String term : searchTerms) {
                    String pattern = "%" + term.toLowerCase() + "%";

                    keywordConditions.add(cb.like(cb.lower(root.get("name")), pattern));
                    keywordConditions.add(cb.like(cb.lower(root.get("brand")), pattern));
                    keywordConditions.add(cb.like(cb.lower(category.get("name")), pattern));
                    keywordConditions.add(cb.like(cb.lower(parent.get("name")), pattern));
                }

                conditions.add(
                        cb.or(keywordConditions.toArray(new Predicate[0]))
                );
            }

            addCsvLikeFilter(
                    conditions,
                    cb,
                    root,
                    "skinType",
                    skinType
            );

            addCsvLikeFilter(
                    conditions,
                    cb,
                    root,
                    "personalColor",
                    personalColor
            );

            if (aiApplyEnabled != null && !aiApplyEnabled.isBlank()) {
                conditions.add(
                        cb.equal(
                                root.get("aiApplyEnabled"),
                                aiApplyEnabled.trim()
                        )
                );
            }

            return cb.and(conditions.toArray(new Predicate[0]));
        };
    }


    /**
     * comma-separated 요청값을 OR LIKE 조건으로 변환한다.
     *
     * requestValue = "건성,복합성"
     * ->
     * column LIKE '%건성%' OR column LIKE '%복합성%'
     */
    private void addCsvLikeFilter(
            List<Predicate> conditions,
            CriteriaBuilder cb,
            Root<Product> root,
            String fieldName,
            String requestValue
    ) {
        if (requestValue == null || requestValue.isBlank()) {
            return;
        }

        List<Predicate> orConditions = Arrays.stream(requestValue.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(value ->
                        cb.like(
                                cb.lower(root.get(fieldName)),
                                "%" + value.toLowerCase() + "%"
                        )
                )
                .toList();

        if (!orConditions.isEmpty()) {
            conditions.add(
                    cb.or(orConditions.toArray(new Predicate[0]))
            );
        }
    }

    private Sort getSort(String sort){
        if("priceLow".equals(sort)) return Sort.by(Sort.Direction.ASC,"price");
        if("priceHigh".equals(sort)) return Sort.by(Sort.Direction.DESC,"price");
        return Sort.by(Sort.Direction.DESC,"createdAt");
    }

    @Transactional(readOnly=true) public ProductResponseDto findById(Long id){ return ProductResponseDto.from(repository.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found. id="+id))); }

    @Transactional public ProductResponseDto save(ProductRequestDto d){
        Category category=categoryRepository.findById(d.getCategoryId()).orElseThrow(()->new IllegalArgumentException("Category not found. id="+d.getCategoryId()));
        Product p=Product.builder().category(category).name(d.getName()).shortDescription(d.getShortDescription()).description(d.getDescription()).price(d.getPrice())
                .discount(d.getDiscount()==null?0:d.getDiscount()).brand(d.getBrand()).usageInfo(d.getUsageInfo())
                .aiApplyEnabled(d.getAiApplyEnabled()==null?"Y":d.getAiApplyEnabled()).productType(d.getProductType()).volume(d.getVolume())
                .skinType(d.getSkinType()).personalColor(d.getPersonalColor()).status(d.getStatus()==null?"ON_SALE":d.getStatus()).build();
        return ProductResponseDto.from(repository.save(p));
    }

    @Transactional public ProductResponseDto update(Long id,ProductRequestDto d){
        Product p=repository.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found. id="+id));
        Category c=categoryRepository.findById(d.getCategoryId()).orElseThrow(()->new IllegalArgumentException("Category not found. id="+d.getCategoryId()));
        p.update(c,d.getName(),d.getShortDescription(),d.getDescription(),d.getPrice(),d.getDiscount(),d.getBrand(),d.getUsageInfo(),d.getAiApplyEnabled(),d.getProductType(),d.getVolume(),d.getSkinType(),d.getPersonalColor(),d.getStatus());
        return ProductResponseDto.from(p);
    }
    @Transactional public void stopSale(Long id){ repository.findById(id).orElseThrow().stopSale(); }
    @Transactional public void resumeSale(Long id){ repository.findById(id).orElseThrow().resumeSale(); }
}
