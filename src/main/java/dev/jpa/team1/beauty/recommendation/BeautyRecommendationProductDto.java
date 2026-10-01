package dev.jpa.team1.beauty.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeautyRecommendationProductDto {

    private Long recommendProductId;
    private Long productId;
    private Long productColorId;

    private String productName;
    private String brand;
    private String productType;

    private Integer price;
    private Integer discount;

    private String colorName;
    private String colorCode;
    private String baseColorGroup;

    private String imagePath;

    private String itemType;
    private Integer sortOrder;
    private String reason;
}
