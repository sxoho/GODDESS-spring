package dev.jpa.team1.beauty.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeautyRecommendationRequestDto {

    private Long memberId;

    /**
     * 예: PERSONAL_COLOR, SKIN_TYPE, COMBINED
     */
    private String recommendationType;

    /**
     * 예: DAILY, CLEAN, ROMANTIC, GLAM
     */
    private String mood;

    /**
     * null/빈 값이면 MEMBER.SKIN_TYPE 사용
     */
    private String skinType;

    /**
     * null/빈 값이면 MEMBER.PERSONAL_COLOR 사용
     */
    private String personalColor;

    /**
     * 특정 상품 타입만 추천할 때 사용
     * 예: LIP, BASE, BLUSH, EYE
     * null이면 전체 상품 타입
     */
    private String itemType;

    /**
     * 추천 상품 개수. 기본 6, 최대 20
     */
    private Integer limit;
}
