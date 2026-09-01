package dev.jpa.team1.fitting.coordfittingitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CoordFittingItemDto {

    @NotNull(message = "상품 색상 ID는 필수값입니다.")
    private Long productColorId;

    private Long productOptionId;

    @NotBlank(message = "상품 역할(상의/하의/아우터 등)은 필수값입니다.")
    private String itemType;

    @NotNull(message = "적용 순서는 필수값입니다.")
    private Integer sortOrder;
}