package dev.jpa.team1.product.productoption;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionRequestDto {

    private Long productColorId;
    private Long productVariantId;

    @Min(0)
    private Integer addPrice;

    @Min(0)
    private Integer stock;

    @Pattern(regexp = "Y|N", message = "visible은 Y 또는 N이어야 합니다.")
    private String visible = "Y";
}
