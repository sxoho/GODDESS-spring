package dev.jpa.team1.product.productsize;

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
public class ProductSizeRequestDto {
    @NotBlank
    @Size(max = 50)
    private String sizeName;

    @Size(max = 255)
    private String sizeDescription;

    @NotBlank
    @Size(max = 1)
    private String visible;

    @NotNull
    private Integer sortOrder;
}
