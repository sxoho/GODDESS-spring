package dev.jpa.team1.product.productcolor;

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
public class ProductColorRequestDto {
    @NotNull
    private Long colorId;

    @NotBlank
    @Size(max = 1)
    private String isMain;

    @NotNull
    private Integer sortOrder;

    @NotBlank
    @Size(max = 1)
    private String fittingEnabled;

    @NotBlank
    @Size(max = 1)
    private String visible;
}
