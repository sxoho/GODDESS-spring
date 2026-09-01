package dev.jpa.team1.product.productcolorinfo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorInfoRequestDto {
    @NotBlank
    private String colorName;

    @NotBlank
    private String colorCode;
}
