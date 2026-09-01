package dev.jpa.team1.recommend.stylerecommendation;

import dev.jpa.team1.recommend.recommendproduct.RecommendProductResponseDto;
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
public class StyleRecommendationResponseDto {
    private Long id;

    private Long memberId;

    private String occasion;

    private String mood;

    private String aiDescription;

    private List<RecommendProductResponseDto> products;

    private LocalDateTime createdAt;


    public static StyleRecommendationResponseDto from(StyleRecommendation recommendation) {
        return new StyleRecommendationResponseDto(
                recommendation.getId(),
                recommendation.getMember().getId(),
                recommendation.getOccasion(),
                recommendation.getMood(),
                recommendation.getAiDescription(),
                List.of(), // 추천 상품 목록은 별도로 세팅
                recommendation.getCreatedAt()
        );
    }

    public static StyleRecommendationResponseDto from(StyleRecommendation recommendation,
                                                       List<RecommendProductResponseDto> products) {
        return new StyleRecommendationResponseDto(
                recommendation.getId(),
                recommendation.getMember().getId(),
                recommendation.getOccasion(),
                recommendation.getMood(),
                recommendation.getAiDescription(),
                products,
                recommendation.getCreatedAt()
        );
    }

}
