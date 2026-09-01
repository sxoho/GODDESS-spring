package dev.jpa.team1.product.productcolor;

import dev.jpa.team1.product.productcolorimage.ProductColorImageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorResponseDto {
    private Long id;

    private Long productId;

    private Long colorId;

    private String colorName;

    private String colorCode;

    private String isMain;

    private Integer sortOrder;

    private String fittingEnabled;

    private String visible;

    private List<ProductColorImageResponseDto> images;


    public static ProductColorResponseDto from(ProductColor color) {
        return new ProductColorResponseDto(
                color.getId(),
                color.getProduct().getId(),
                color.getProductColorInfo().getId(),
                color.getProductColorInfo().getColorName(),
                color.getProductColorInfo().getColorCode(),
                color.getIsMain(),
                color.getSortOrder(),
                color.getFittingEnabled(),
                color.getVisible(),
                List.of()
        );
    }

    public static ProductColorResponseDto from(ProductColor color,
                                                List<ProductColorImageResponseDto> images) {
        return new ProductColorResponseDto(
                color.getId(),
                color.getProduct().getId(),
                color.getProductColorInfo().getId(),
                color.getProductColorInfo().getColorName(),
                color.getProductColorInfo().getColorCode(),
                color.getIsMain(),
                color.getSortOrder(),
                color.getFittingEnabled(),
                color.getVisible(),
                images
        );
    }

}
