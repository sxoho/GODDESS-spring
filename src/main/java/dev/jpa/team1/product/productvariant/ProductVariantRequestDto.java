package dev.jpa.team1.product.productvariant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductVariantRequestDto {
    @NotBlank @Size(max = 50)
    private String variantName;
    @Size(max = 255)
    private String variantDescription;
    @Size(max = 1)
    private String visible = "Y";
    private Integer sortOrder = 1;
}
