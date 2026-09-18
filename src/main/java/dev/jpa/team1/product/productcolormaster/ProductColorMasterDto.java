package dev.jpa.team1.product.productcolormaster;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorMasterDto {
    private Long id;
    private String colorName;
    private String colorCode;
    private String baseColorGroup;

    public static ProductColorMasterDto from(ProductColorMaster e) {
        return new ProductColorMasterDto(
                e.getId(),
                e.getColorName(),
                e.getColorCode(),
                e.getBaseColorGroup()
        );
    }
}
