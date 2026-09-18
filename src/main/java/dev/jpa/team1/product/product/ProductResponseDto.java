package dev.jpa.team1.product.product;

import dev.jpa.team1.product.productcolor.ProductColorResponseDto;
import dev.jpa.team1.product.productvariant.ProductVariantResponseDto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private Long categoryId;
    private Long parentCategoryId;
    private String categoryName;
    private String name;
    private String shortDescription;
    private String description;
    private Integer price;
    private Integer discount;
    private String brand;
    private String usageInfo;
    private String aiApplyEnabled;
    private String productType;
    private String volume;
    private String skinType;
    private String personalColor;
    private String status;
    private Integer viewCount;
    private List<ProductColorResponseDto> colors;
    private List<ProductVariantResponseDto> variants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponseDto from(Product p) {
        return new ProductResponseDto(p.getId(), p.getCategory().getId(),
                p.getCategory().getParent()!=null?p.getCategory().getParent().getId():null,
                p.getCategory().getName(), p.getName(), p.getShortDescription(), p.getDescription(), p.getPrice(), p.getDiscount(), p.getBrand(),
                p.getUsageInfo(), p.getAiApplyEnabled(), p.getProductType(), p.getVolume(), p.getSkinType(), p.getPersonalColor(),
                p.getStatus(), p.getViewCount(), List.of(), List.of(), p.getCreatedAt(), p.getUpdatedAt());
    }
}
