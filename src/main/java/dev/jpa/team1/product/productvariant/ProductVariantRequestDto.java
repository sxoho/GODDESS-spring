package dev.jpa.team1.product.productvariant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantRequestDto {

    @NotBlank
    @Size(max = 50)
    private String variantName;

    @Size(max = 255)
    private String variantDescription;

    @Pattern(regexp = "Y|N", message = "visible은 Y 또는 N이어야 합니다.")
    private String visible = "Y";

    @Positive
    private Integer sortOrder = 1;
}
