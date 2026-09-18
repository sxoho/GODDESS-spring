package dev.jpa.team1.product.product;

import dev.jpa.team1.product.productcolor.ProductColorRequestDto;
import dev.jpa.team1.product.productoption.ProductOptionRequestDto;
import dev.jpa.team1.product.productvariant.ProductVariantRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductRequestDto {
    @NotNull private Long categoryId;
    @NotBlank @Size(max=200) private String name;
    @Size(max=150) private String shortDescription;
    private String description;
    @NotNull @Min(0) private Integer price;
    @Min(0) @Max(100) private Integer discount;
    @Size(max=100) private String brand;
    @Size(max=500) private String usageInfo;
    @Size(max=1) private String aiApplyEnabled;
    @Size(max=30) private String productType;
    @Size(max=50) private String volume;
    @Size(max=100) private String skinType;
    @Size(max=100) private String personalColor;
    @Size(max=20) private String status;
    @Valid private List<ProductColorRequestDto> colors;
    @Valid private List<ProductVariantRequestDto> variants;
    @Valid private List<ProductOptionRequestDto> options;
}
