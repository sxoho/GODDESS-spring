package dev.jpa.team1.product.productoption;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionRequestDto {
    @NotNull
    private Long productColorId;

    @NotNull
    private Long productSizeId;

    private Integer addPrice;

    private Integer stock;

    @NotBlank
    @Size(max = 1)
    private String visible;
}
