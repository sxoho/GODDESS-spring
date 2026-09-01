package dev.jpa.team1.recommend.recommendproduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendProductResponseDto {
    private Long id;

    private Long styleRecommendationId;

    private Long productColorId;

    private String productName;

    private String colorName;

    private String mainImagePath;

    private String itemType;

    private Integer sortOrder;

    private String reason;


    public static RecommendProductResponseDto from(RecommendProduct recommendProduct, String mainImagePath) {
        return new RecommendProductResponseDto(
                recommendProduct.getId(),
                recommendProduct.getStyleRecommendation().getId(),
                recommendProduct.getProductColor().getId(),
                recommendProduct.getProductColor().getProduct().getName(),
                recommendProduct.getProductColor().getProductColorInfo().getColorName(),
                mainImagePath,
                recommendProduct.getItemType(),
                recommendProduct.getSortOrder(),
                recommendProduct.getReason()
        );
    }

}
