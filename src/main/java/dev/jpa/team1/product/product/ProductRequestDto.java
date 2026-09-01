package dev.jpa.team1.product.product;

import dev.jpa.team1.product.productcolor.ProductColorRequestDto;
import dev.jpa.team1.product.productoption.ProductOptionRequestDto;
import dev.jpa.team1.product.productsize.ProductSizeRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotNull
    private Long categoryId;
    
    private String brand;

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    private Integer price;

    @Size(max = 50)
    private String discount;

    @Size(max = 255)
    private String material;

    @Size(max = 255)
    private String washing;

    @NotBlank
    @Size(max = 1)
    private String fittingEnabled;

    @NotBlank
    @Size(max = 20)
    private String status;

    @Valid
    private List<ProductColorRequestDto> colors;

    @Valid
    private List<ProductSizeRequestDto> sizes;

    @Valid
    private List<ProductOptionRequestDto> options;
}
