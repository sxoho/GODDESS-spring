package dev.jpa.team1.product.productsize;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import dev.jpa.team1.product.productoption.ProductOptionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSizeService {

    private final ProductSizeRepository repository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    // 전체 조회
    @Transactional(readOnly = true)
    public List<ProductSizeResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(ProductSizeResponseDto::from)
                .toList();
    }

    // 특정 상품의 사이즈 목록 조회
    @Transactional(readOnly = true)
    public List<ProductSizeResponseDto> findByProductId(Long productId) {
        return repository
                .findByProductIdOrderBySortOrderAsc(productId)
                .stream()
                .map(ProductSizeResponseDto::from)
                .toList();
    }

    // 사이즈 단건 조회
    @Transactional(readOnly = true)
    public ProductSizeResponseDto findById(Long id) {

        ProductSize entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductSize not found. id=" + id
                        )
                );

        return ProductSizeResponseDto.from(entity);
    }

    // 사이즈 등록
    @Transactional
    public ProductSizeResponseDto save(
            Long productId,
            ProductSizeRequestDto requestDto
    ) {

        // 상품 존재 여부 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found. id=" + productId
                        )
                );

        // 동일 상품에 동일 사이즈 중복 등록 방지
        boolean exists =
                repository.existsByProductIdAndSizeName(
                        productId,
                        requestDto.getSizeName()
                );

        if (exists) {
            throw new IllegalArgumentException(
                    "이미 등록된 사이즈입니다. sizeName="
                            + requestDto.getSizeName()
            );
        }

        // Entity 생성
        ProductSize entity = ProductSize.builder()
                .product(product)
                .sizeName(requestDto.getSizeName())
                .sizeDescription(requestDto.getSizeDescription())
                .visible(requestDto.getVisible())
                .sortOrder(requestDto.getSortOrder())
                .build();

        // DB 저장
        ProductSize saved = repository.save(entity);

        // DTO 반환
        return ProductSizeResponseDto.from(saved);
    }

    // 사이즈 수정
    @Transactional
    public ProductSizeResponseDto update(
            Long id,
            ProductSizeRequestDto requestDto
    ) {
        ProductSize entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductSize not found. id=" + id
                ));

        entity.update(
                requestDto.getSizeName(),
                requestDto.getSizeDescription(),
                requestDto.getVisible(),
                requestDto.getSortOrder()
        );

        return ProductSizeResponseDto.from(repository.saveAndFlush(entity));
    }

    // 사이즈 삭제
    @Transactional
    public void delete(Long id) {

        ProductSize entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductSize not found. id=" + id
                        )
                );

        // PRODUCT_OPTION이 PRODUCT_SIZE를 참조하므로 옵션을 먼저 삭제한다.
        productOptionRepository.deleteByProductSizeId(id);
        repository.delete(entity);
    }
}