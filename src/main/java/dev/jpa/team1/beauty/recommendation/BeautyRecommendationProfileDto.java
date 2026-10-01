package dev.jpa.team1.beauty.recommendation;

import dev.jpa.team1.member.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeautyRecommendationProfileDto {

    private Long memberId;
    private String name;
    private String nickname;
    private String skinType;
    private String personalColor;

    public static BeautyRecommendationProfileDto from(Member member) {
        return new BeautyRecommendationProfileDto(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getSkinType(),
                member.getPersonalColor()
        );
    }
}
