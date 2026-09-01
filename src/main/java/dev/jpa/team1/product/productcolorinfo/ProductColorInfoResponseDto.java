package dev.jpa.team1.product.productcolorinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorInfoResponseDto {
    private Long id;

    private String colorName;

    private String colorCode;


    public static ProductColorInfoResponseDto from(ProductColorInfo info) {
        return new ProductColorInfoResponseDto(
                info.getId(),
                info.getColorName(),
                info.getColorCode()
        );
    }

}
