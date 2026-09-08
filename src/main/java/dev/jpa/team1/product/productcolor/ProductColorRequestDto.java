package dev.jpa.team1.product.productcolor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorRequestDto {

    @NotNull
    private Long colorId;

    @Pattern(regexp = "Y|N", message = "isMain은 Y 또는 N이어야 합니다.")
    private String isMain = "N";

    @Positive
    private Integer sortOrder = 1;

    @Pattern(regexp = "Y|N", message = "aiApplyEnabled는 Y 또는 N이어야 합니다.")
    private String aiApplyEnabled = "Y";

    @Pattern(regexp = "Y|N", message = "visible은 Y 또는 N이어야 합니다.")
    private String visible = "Y";
}
