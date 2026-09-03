package dev.jpa.team1.product.productvariant;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository repository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductVariantResponseDto> findByProductId(Long productId) {
        return repository.findByProductIdOrderBySortOrderAsc(productId).stream().map(ProductVariantResponseDto::from).toList();
    }

    @Transactional
    public ProductVariantResponseDto save(Long productId, ProductVariantRequestDto dto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found. id=" + productId));
        if (repository.existsByProductIdAndVariantName(productId, dto.getVariantName())) throw new IllegalArgumentException("이미 등록된 상품 옵션명입니다.");
        ProductVariant v = ProductVariant.builder().product(product).variantName(dto.getVariantName()).variantDescription(dto.getVariantDescription()).visible(dto.getVisible() == null ? "Y" : dto.getVisible()).sortOrder(dto.getSortOrder() == null ? 1 : dto.getSortOrder()).build();
        return ProductVariantResponseDto.from(repository.save(v));
    }

    @Transactional
    public ProductVariantResponseDto update(Long id, ProductVariantRequestDto dto) {
        ProductVariant v = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ProductVariant not found. id=" + id));
        v.update(dto.getVariantName(), dto.getVariantDescription(), dto.getVisible() == null ? "Y" : dto.getVisible(), dto.getSortOrder());
        return ProductVariantResponseDto.from(v);
    }

    @Transactional public void delete(Long id) { repository.deleteById(id); }
}
