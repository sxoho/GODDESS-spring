package dev.jpa.team1.beauty.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeautyRecommendationResponseDto {

    private Long recommendationId;
    private Long memberId;

    private String recommendationType;
    private String mood;
    private String skinType;
    private String personalColor;
    private String aiDescription;

    private LocalDateTime createdAt;

    private List<BeautyRecommendationProductDto> products;
}
