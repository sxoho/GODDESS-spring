package dev.jpa.team1.recommend.recommendproduct;

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
public class RecommendProductRequestDto {
    @NotNull
    private Long styleRecommendationId;

    @NotNull
    private Long productColorId;

    @NotBlank
    @Size(max = 50)
    private String itemType;

    @NotNull
    private Integer sortOrder;

    @Size(max = 255)
    private String reason;
}
