package dev.jpa.team1.product.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDto {
    private Long id;

    private String name;

    private Integer price;

    private String discount;

    private String categoryName;

    private String mainImagePath;

    private String fittingEnabled;

    private String status;


    public static ProductListResponseDto from(Product product, String mainImagePath) {
        return new ProductListResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDiscount(),
                product.getCategory().getName(),
                mainImagePath,
                product.getFittingEnabled(),
                product.getStatus()
        );
    }

}
