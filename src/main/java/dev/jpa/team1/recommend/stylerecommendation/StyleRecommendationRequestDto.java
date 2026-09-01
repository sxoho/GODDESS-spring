package dev.jpa.team1.recommend.stylerecommendation;

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
public class StyleRecommendationRequestDto {
    @NotNull
    private Long memberId;

    @NotBlank
    @Size(max = 100)
    private String occasion;

    @NotBlank
    @Size(max = 100)
    private String mood;
}
