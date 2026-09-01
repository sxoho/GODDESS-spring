package dev.jpa.team1.product.productoption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionResponseDto {
    private Long id;

    private Long productId;

    private Long productColorId;

    private String colorName;

    private Long productSizeId;

    private String sizeName;

    private Integer addPrice;

    private Integer stock;

    private String visible;


    public static ProductOptionResponseDto from(ProductOption option) {
        return new ProductOptionResponseDto(
                option.getId(),
                option.getProduct().getId(),
                option.getProductColor().getId(),
                option.getProductColor().getProductColorInfo().getColorName(),
                option.getProductSize().getId(),
                option.getProductSize().getSizeName(),
                option.getAddPrice(),
                option.getStock(),
                option.getVisible()
        );
    }

}
