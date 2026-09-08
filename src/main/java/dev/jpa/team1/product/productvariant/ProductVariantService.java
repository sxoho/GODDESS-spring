package dev.jpa.team1.product.productvariant;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import dev.jpa.team1.product.productoption.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository repository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository optionRepository;

    @Transactional(readOnly = true)
    public List<ProductVariantResponseDto> findByProductId(Long productId) {
        return repository.findByProductIdOrderBySortOrderAsc(productId)
                .stream()
                .map(ProductVariantResponseDto::from)
                .toList();
    }

    @Transactional
    public ProductVariantResponseDto save(
            Long productId,
            ProductVariantRequestDto dto
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found. id=" + productId
                        )
                );

        String variantName = dto.getVariantName().trim();

        if (repository.existsByProductIdAndVariantName(productId, variantName)) {
            throw new IllegalArgumentException("이미 등록된 상품 규격명입니다.");
        }

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .variantName(variantName)
                .variantDescription(trimToNull(dto.getVariantDescription()))
                .visible(dto.getVisible() == null ? "Y" : dto.getVisible())
                .sortOrder(dto.getSortOrder() == null ? 1 : dto.getSortOrder())
                .build();

        return ProductVariantResponseDto.from(repository.save(variant));
    }

    @Transactional
    public ProductVariantResponseDto update(
            Long id,
            ProductVariantRequestDto dto
    ) {
        ProductVariant variant = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductVariant not found. id=" + id
                        )
                );

        String variantName = dto.getVariantName().trim();

        repository.findByProductIdOrderBySortOrderAsc(variant.getProduct().getId())
                .stream()
                .filter(item -> !item.getId().equals(id))
                .filter(item -> item.getVariantName().equalsIgnoreCase(variantName))
                .findFirst()
                .ifPresent(item -> {
                    throw new IllegalArgumentException("이미 등록된 상품 규격명입니다.");
                });

        variant.update(
                variantName,
                trimToNull(dto.getVariantDescription()),
                dto.getVisible() == null ? "Y" : dto.getVisible(),
                dto.getSortOrder()
        );

        return ProductVariantResponseDto.from(variant);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("ProductVariant not found. id=" + id);
        }

        // PRODUCT_OPTION이 PRODUCT_VARIANT를 FK로 참조하므로 먼저 정리한다.
        optionRepository.deleteByProductVariantId(id);
        repository.deleteById(id);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
