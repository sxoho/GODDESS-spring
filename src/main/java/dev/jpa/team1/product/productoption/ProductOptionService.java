package dev.jpa.team1.product.productoption;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productcolor.ProductColorRepository;
import dev.jpa.team1.product.productvariant.ProductVariant;
import dev.jpa.team1.product.productvariant.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository repository;
    private final ProductRepository productRepository;
    private final ProductColorRepository colorRepository;
    private final ProductVariantRepository variantRepository;

    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> findAll() {
        return repository.findAll().stream()
                .map(ProductOptionResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> findByProductId(Long productId) {
        return repository.findByProductIdOrderByIdAsc(productId).stream()
                .map(ProductOptionResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> findAvailableByProductId(Long productId) {
        return repository
                .findByProductIdAndVisibleAndStockGreaterThanOrderByIdAsc(
                        productId,
                        "Y",
                        0
                )
                .stream()
                .map(ProductOptionResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductOptionResponseDto findById(Long id) {
        return ProductOptionResponseDto.from(
                repository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "ProductOption not found. id=" + id
                                )
                        )
        );
    }

    @Transactional
    public ProductOptionResponseDto save(
            Long productId,
            ProductOptionRequestDto dto
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found. id=" + productId
                        )
                );

        ProductColor color = getColor(dto.getProductColorId(), productId);
        ProductVariant variant = getVariant(dto.getProductVariantId(), productId);

        if (repository.existsByProductIdAndProductColorIdAndProductVariantId(
                productId,
                dto.getProductColorId(),
                dto.getProductVariantId()
        )) {
            throw new IllegalArgumentException("이미 등록된 판매 옵션입니다.");
        }

        ProductOption option = ProductOption.builder()
                .product(product)
                .productColor(color)
                .productVariant(variant)
                .addPrice(dto.getAddPrice() == null ? 0 : dto.getAddPrice())
                .stock(dto.getStock() == null ? 0 : dto.getStock())
                .visible(dto.getVisible() == null ? "Y" : dto.getVisible())
                .build();

        return ProductOptionResponseDto.from(repository.save(option));
    }

    @Transactional
    public ProductOptionResponseDto update(
            Long id,
            ProductOptionRequestDto dto
    ) {
        ProductOption option = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductOption not found. id=" + id
                        )
                );

        Long productId = option.getProduct().getId();

        option.update(
                getColor(dto.getProductColorId(), productId),
                getVariant(dto.getProductVariantId(), productId),
                dto.getAddPrice(),
                dto.getStock(),
                dto.getVisible()
        );

        return ProductOptionResponseDto.from(option);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("ProductOption not found. id=" + id);
        }
        repository.deleteById(id);
    }

    private ProductColor getColor(Long id, Long productId) {
        if (id == null) {
            return null;
        }

        ProductColor color = colorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductColor not found. id=" + id
                        )
                );

        if (!color.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("해당 상품의 색상이 아닙니다.");
        }

        return color;
    }

    private ProductVariant getVariant(Long id, Long productId) {
        if (id == null) {
            return null;
        }

        ProductVariant variant = variantRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductVariant not found. id=" + id
                        )
                );

        if (!variant.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("해당 상품의 규격이 아닙니다.");
        }

        return variant;
    }
}
