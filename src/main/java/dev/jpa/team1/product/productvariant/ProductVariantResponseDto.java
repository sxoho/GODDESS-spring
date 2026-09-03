package dev.jpa.team1.product.productvariant;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductVariantResponseDto {
    private Long id;
    private Long productId;
    private String variantName;
    private String variantDescription;
    private String visible;
    private Integer sortOrder;

    public static ProductVariantResponseDto from(ProductVariant v) {
        return new ProductVariantResponseDto(v.getId(), v.getProduct().getId(), v.getVariantName(), v.getVariantDescription(), v.getVisible(), v.getSortOrder());
    }
}
