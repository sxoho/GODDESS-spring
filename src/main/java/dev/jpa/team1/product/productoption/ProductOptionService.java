package dev.jpa.team1.product.productoption;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productcolor.ProductColorRepository;
import dev.jpa.team1.product.productsize.ProductSize;
import dev.jpa.team1.product.productsize.ProductSizeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository repository;
    private final ProductRepository productRepository;
    private final ProductColorRepository productColorRepository;
    private final ProductSizeRepository productSizeRepository;

    // 전체 옵션 조회
    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(ProductOptionResponseDto::from)
                .toList();
    }

    // 특정 상품의 옵션 조회
    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> findByProductId(Long productId) {
        return repository
                .findByProductIdOrderByIdAsc(productId)
                .stream()
                .map(ProductOptionResponseDto::from)
                .toList();
    }

    // 특정 상품의 판매 가능 옵션 조회
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

    // 옵션 단건 조회
    @Transactional(readOnly = true)
    public ProductOptionResponseDto findById(Long id) {
        ProductOption entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductOption not found. id=" + id
                        )
                );

        return ProductOptionResponseDto.from(entity);
    }

    // 옵션 등록
    @Transactional
    public ProductOptionResponseDto save(
            Long productId,
            ProductOptionRequestDto requestDto
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found. id=" + productId
                        )
                );

        ProductColor productColor =
                productColorRepository.findById(requestDto.getProductColorId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "ProductColor not found. id="
                                                + requestDto.getProductColorId()
                                )
                        );

        ProductSize productSize =
                productSizeRepository.findById(requestDto.getProductSizeId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "ProductSize not found. id="
                                                + requestDto.getProductSizeId()
                                )
                        );

        // 선택한 색상이 해당 상품의 색상인지 확인
        if (!productColor.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException(
                    "해당 상품의 색상이 아닙니다."
            );
        }

        // 선택한 사이즈가 해당 상품의 사이즈인지 확인
        if (!productSize.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException(
                    "해당 상품의 사이즈가 아닙니다."
            );
        }

        // 같은 상품 + 색상 + 사이즈 조합 중복 방지
        boolean exists =
                repository.existsByProductIdAndProductColorIdAndProductSizeId(
                        productId,
                        requestDto.getProductColorId(),
                        requestDto.getProductSizeId()
                );

        if (exists) {
            throw new IllegalArgumentException(
                    "이미 등록된 상품 옵션입니다."
            );
        }

        ProductOption entity = ProductOption.builder()
                .product(product)
                .productColor(productColor)
                .productSize(productSize)
                .addPrice(
                        requestDto.getAddPrice() == null
                                ? 0
                                : requestDto.getAddPrice()
                )
                .stock(
                        requestDto.getStock() == null
                                ? 0
                                : requestDto.getStock()
                )
                .visible(requestDto.getVisible())
                .build();

        ProductOption saved = repository.save(entity);

        return ProductOptionResponseDto.from(saved);
    }

    // 옵션 수정
    @Transactional
    public ProductOptionResponseDto update(
            Long id,
            ProductOptionRequestDto requestDto
    ) {
        ProductOption entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductOption not found. id=" + id
                ));

        ProductColor productColor = productColorRepository
                .findById(requestDto.getProductColorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductColor not found. id=" + requestDto.getProductColorId()
                ));

        ProductSize productSize = productSizeRepository
                .findById(requestDto.getProductSizeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductSize not found. id=" + requestDto.getProductSizeId()
                ));

        Long productId = entity.getProduct().getId();

        if (!productColor.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("해당 상품의 색상이 아닙니다.");
        }

        if (!productSize.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("해당 상품의 사이즈가 아닙니다.");
        }

        entity.update(
                productColor,
                productSize,
                requestDto.getAddPrice(),
                requestDto.getStock(),
                requestDto.getVisible()
        );

        return ProductOptionResponseDto.from(repository.saveAndFlush(entity));
    }

    // 옵션 삭제
    @Transactional
    public void delete(Long id) {
        ProductOption entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductOption not found. id=" + id
                        )
                );

        repository.delete(entity);
    }
}