package dev.jpa.team1.product.product;

import dev.jpa.team1.product.category.Category;
import dev.jpa.team1.product.category.CategoryRepository;
import dev.jpa.team1.product.search.ProductSearchKeyword;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll() {
        return repository.findAll().stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findPage(
            int page,
            int size,
            Long mainCategoryId,
            Long categoryId,
            String sort,
            String keyword
    ) {
        return repository.findAll(
                        buildSearchSpecification(
                                mainCategoryId,
                                categoryId,
                                terms(keyword),
                                true
                        ),
                        PageRequest.of(page, size, getSort(sort))
                )
                .map(ProductResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAdminPage(
            int page,
            int size,
            Long mainCategoryId,
            Long categoryId,
            String sort,
            String keyword
    ) {
        return repository.findAll(
                        buildSearchSpecification(
                                mainCategoryId,
                                categoryId,
                                terms(keyword),
                                false
                        ),
                        PageRequest.of(page, size, getSort(sort))
                )
                .map(ProductResponseDto::from);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findBest100(String keyword) {
        return repository.findAll(
                        buildSearchSpecification(null, null, terms(keyword), true),
                        PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "viewCount"))
                )
                .getContent()
                .stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    private List<String> terms(String keyword) {
        String value = keyword == null ? "" : keyword.trim();
        return value.isBlank()
                ? List.of()
                : ProductSearchKeyword.buildSearchTerms(value);
    }

    private Specification<Product> buildSearchSpecification(
            Long mainCategoryId,
            Long categoryId,
            List<String> searchTerms,
            boolean publicOnly
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
            } else if (mainCategoryId != null) {
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
                    keywordConditions.add(cb.like(cb.lower(root.get("productType")), pattern));
                    keywordConditions.add(cb.like(cb.lower(root.get("skinType")), pattern));
                    keywordConditions.add(cb.like(cb.lower(root.get("personalColor")), pattern));
                    keywordConditions.add(cb.like(cb.lower(category.get("name")), pattern));
                    keywordConditions.add(cb.like(cb.lower(parent.get("name")), pattern));
                }

                conditions.add(cb.or(keywordConditions.toArray(new Predicate[0])));
            }

            return cb.and(conditions.toArray(new Predicate[0]));
        };
    }

    private Sort getSort(String sort) {
        if ("priceLow".equals(sort)) {
            return Sort.by(Sort.Direction.ASC, "price");
        }
        if ("priceHigh".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "price");
        }
        if ("recommended".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "viewCount")
                    .and(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {
        return ProductResponseDto.from(
                repository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found. id=" + id
                                )
                        )
        );
    }

    @Transactional
    public ProductResponseDto save(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found. id=" + dto.getCategoryId()
                        )
                );

        Product product = Product.builder()
                .category(category)
                .name(dto.getName().trim())
                .description(trimToNull(dto.getDescription()))
                .price(dto.getPrice())
                .discount(dto.getDiscount() == null ? 0 : dto.getDiscount())
                .brand(trimToNull(dto.getBrand()))
                .usageInfo(trimToNull(dto.getUsageInfo()))
                .aiApplyEnabled(defaultYn(dto.getAiApplyEnabled(), "Y"))
                .productType(trimToNull(dto.getProductType()))
                .volume(trimToNull(dto.getVolume()))
                .skinType(trimToNull(dto.getSkinType()))
                .personalColor(trimToNull(dto.getPersonalColor()))
                .status(defaultStatus(dto.getStatus()))
                .build();

        return ProductResponseDto.from(repository.save(product));
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto dto) {
        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found. id=" + id
                        )
                );

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found. id=" + dto.getCategoryId()
                        )
                );

        product.update(
                category,
                dto.getName().trim(),
                trimToNull(dto.getDescription()),
                dto.getPrice(),
                dto.getDiscount(),
                trimToNull(dto.getBrand()),
                trimToNull(dto.getUsageInfo()),
                defaultYn(dto.getAiApplyEnabled(), "Y"),
                trimToNull(dto.getProductType()),
                trimToNull(dto.getVolume()),
                trimToNull(dto.getSkinType()),
                trimToNull(dto.getPersonalColor()),
                defaultStatus(dto.getStatus())
        );

        return ProductResponseDto.from(product);
    }

    @Transactional
    public void stopSale(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found. id=" + id))
                .stopSale();
    }

    @Transactional
    public void resumeSale(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found. id=" + id))
                .resumeSale();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String defaultYn(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    private String defaultStatus(String value) {
        return value == null || value.isBlank() ? "ON_SALE" : value.trim();
    }
}
