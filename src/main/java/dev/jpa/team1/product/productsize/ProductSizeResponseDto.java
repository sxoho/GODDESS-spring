package dev.jpa.team1.product.productsize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeResponseDto {
    private Long id;

    private Long productId;

    private String sizeName;

    private String sizeDescription;

    private String visible;

    private Integer sortOrder;


    public static ProductSizeResponseDto from(ProductSize size) {
        return new ProductSizeResponseDto(
                size.getId(),
                size.getProduct().getId(),
                size.getSizeName(),
                size.getSizeDescription(),
                size.getVisible(),
                size.getSortOrder()
        );
    }

}
