package dev.jpa.team1.product.productcolor;

import dev.jpa.team1.product.product.*;
import dev.jpa.team1.product.productcolormaster.*;
import dev.jpa.team1.product.productcolorimage.*;
import dev.jpa.team1.product.productcolorpersonalcolor.ProductColorPersonalColor;
import dev.jpa.team1.product.productcolorpersonalcolor.ProductColorPersonalColorRepository;
import dev.jpa.team1.product.productoption.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductColorService {
    private static final Set<String> VALID_PERSONAL_COLORS =
            Set.of("SPRING_WARM", "SUMMER_COOL", "AUTUMN_WARM", "WINTER_COOL", "NEUTRAL");

    private final ProductColorRepository repository;
    private final ProductRepository productRepository;
    private final ProductColorMasterRepository colorMasterRepository;
    private final ProductColorImageRepository imageRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductColorPersonalColorRepository personalColorRepository;

    @Transactional(readOnly = true)
    public List<ProductColorResponseDto> findAll() {
        return repository.findAll().stream().map(this::withDetails).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductColorResponseDto> findByProductId(Long id) {
        return repository.findByProductIdOrderBySortOrderAsc(id).stream().map(this::withDetails).toList();
    }

    @Transactional(readOnly = true)
    public ProductColorResponseDto findById(Long id) {
        return withDetails(repository.findById(id).orElseThrow());
    }

    @Transactional
    public ProductColorResponseDto save(Long productId, ProductColorRequestDto d) {
        Product p = productRepository.findById(productId).orElseThrow();
        ProductColorMaster m = colorMasterRepository.findById(d.getColorId()).orElseThrow();

        if (repository.existsByProductIdAndColorMasterId(productId, d.getColorId())) {
            throw new IllegalArgumentException("이미 등록된 상품 색상입니다.");
        }

        ProductColor saved = repository.save(
                ProductColor.builder()
                        .product(p)
                        .colorMaster(m)
                        .isMain(d.getIsMain() == null ? "N" : d.getIsMain())
                        .sortOrder(d.getSortOrder() == null ? 1 : d.getSortOrder())
                        .aiApplyEnabled(d.getAiApplyEnabled() == null ? "Y" : d.getAiApplyEnabled())
                        .visible(d.getVisible() == null ? "Y" : d.getVisible())
                        .build()
        );

        replacePersonalColors(saved, d.getPersonalColors());
        return withDetails(saved);
    }

    @Transactional
    public ProductColorResponseDto update(Long id, ProductColorRequestDto d) {
        ProductColor e = repository.findById(id).orElseThrow();
        ProductColorMaster m = colorMasterRepository.findById(d.getColorId()).orElseThrow();
        e.update(m, d.getIsMain(), d.getSortOrder(), d.getAiApplyEnabled(), d.getVisible());
        replacePersonalColors(e, d.getPersonalColors());
        return withDetails(e);
    }

    private void replacePersonalColors(ProductColor productColor, List<String> requestedColors) {
        /*
         * 수정 시 기존 PRODUCT_COLOR_PERSONAL_COLOR를 먼저 삭제한 뒤
         * 같은 값이 다시 들어올 수 있으므로 DELETE를 DB에 즉시 반영한다.
         *
         * flush()가 없으면 Hibernate가 INSERT를 DELETE보다 먼저 실행하면서
         * UK_PCPC_COLOR_TYPE(productcolor_id, personal_color_type)에 걸릴 수 있다.
         */
        personalColorRepository.deleteByProductColorId(productColor.getId());
        personalColorRepository.flush();

        if (requestedColors == null || requestedColors.isEmpty()) {
            return;
        }

        LinkedHashSet<String> uniqueColors = requestedColors.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        for (String personalColor : uniqueColors) {
            if (!VALID_PERSONAL_COLORS.contains(personalColor)) {
                throw new IllegalArgumentException("지원하지 않는 퍼스널컬러입니다: " + personalColor);
            }

            personalColorRepository.save(
                    ProductColorPersonalColor.builder()
                            .productColor(productColor)
                            .personalColorType(personalColor)
                            .build()
            );
        }
    }

    private ProductColorResponseDto withDetails(ProductColor c) {
        List<String> personalColors = personalColorRepository
                .findByProductColorIdOrderByIdAsc(c.getId())
                .stream()
                .map(ProductColorPersonalColor::getPersonalColorType)
                .toList();

        List<ProductColorImageResponseDto> images = imageRepository
                .findByProductColorIdOrderBySortOrderAsc(c.getId())
                .stream()
                .map(ProductColorImageResponseDto::from)
                .toList();

        return ProductColorResponseDto.from(c, personalColors, images);
    }

    @Transactional
    public void delete(Long id) {
        personalColorRepository.deleteByProductColorId(id);
        optionRepository.deleteByProductColorId(id);
        imageRepository.deleteByProductColorId(id);
        repository.deleteById(id);
    }
}
