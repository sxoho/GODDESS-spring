package dev.jpa.team1.product.productcolor;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductColorRequestDto {
    @NotNull private Long colorId;
    @Size(max=1) private String isMain = "N";
    private Integer sortOrder = 1;
    @Size(max=1) private String aiApplyEnabled = "Y";
    @Size(max=1) private String visible = "Y";
    private List<String> personalColors = new ArrayList<>();
}
