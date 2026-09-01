package dev.jpa.team1.product.productcolor;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import dev.jpa.team1.product.productcolorimage.ProductColorImageRepository;
import dev.jpa.team1.product.productcolorimage.ProductColorImageResponseDto;
import dev.jpa.team1.product.productcolorinfo.ProductColorInfo;
import dev.jpa.team1.product.productcolorinfo.ProductColorInfoRepository;
import dev.jpa.team1.product.productoption.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductColorService {

    private final ProductColorRepository repository;
    private final ProductRepository productRepository;
    private final ProductColorInfoRepository productColorInfoRepository;
    private final ProductColorImageRepository productColorImageRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional(readOnly = true)
    public List<ProductColorResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseWithImages)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductColorResponseDto> findByProductId(Long productId) {
        return repository.findByProductIdOrderBySortOrderAsc(productId).stream()
                .map(this::toResponseWithImages)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductColorResponseDto findById(Long id) {
        ProductColor entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductColor not found. id=" + id
                ));
        return toResponseWithImages(entity);
    }

    @Transactional
    public ProductColorResponseDto save(Long productId, ProductColorRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found. id=" + productId
                ));

        ProductColorInfo colorInfo = productColorInfoRepository
                .findById(requestDto.getColorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Color not found. id=" + requestDto.getColorId()
                ));

        ProductColor entity = ProductColor.builder()
                .product(product)
                .productColorInfo(colorInfo)
                .isMain(requestDto.getIsMain())
                .sortOrder(requestDto.getSortOrder())
                .fittingEnabled(requestDto.getFittingEnabled())
                .visible(requestDto.getVisible())
                .build();

        return toResponseWithImages(repository.save(entity));
    }

    @Transactional
    public ProductColorResponseDto update(Long id, ProductColorRequestDto requestDto) {
        ProductColor entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductColor not found. id=" + id
                ));

        ProductColorInfo colorInfo = productColorInfoRepository
                .findById(requestDto.getColorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Color not found. id=" + requestDto.getColorId()
                ));

        entity.update(
                colorInfo,
                requestDto.getIsMain(),
                requestDto.getSortOrder(),
                requestDto.getFittingEnabled(),
                requestDto.getVisible()
        );

        return toResponseWithImages(repository.saveAndFlush(entity));
    }

    private ProductColorResponseDto toResponseWithImages(ProductColor color) {
        List<ProductColorImageResponseDto> images = productColorImageRepository
                .findByProductColorIdOrderBySortOrderAsc(color.getId())
                .stream()
                .map(ProductColorImageResponseDto::from)
                .toList();

        return ProductColorResponseDto.from(color, images);
    }


    @Transactional
    public void delete(Long id) {
        ProductColor entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductColor not found. id=" + id
                ));

        // FK 제약조건 때문에 옵션과 이미지 DB 데이터를 먼저 정리한다.
        productOptionRepository.deleteByProductColorId(id);
        productColorImageRepository.deleteByProductColorId(id);
        repository.delete(entity);
    }

}
