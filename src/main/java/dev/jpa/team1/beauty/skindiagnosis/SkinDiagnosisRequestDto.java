package dev.jpa.team1.beauty.skindiagnosis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkinDiagnosisRequestDto {

    private Long memberId;

    private String skinType;

    private Integer dryScore;

    private Integer oilyScore;

    private Integer combinationScore;

    private Integer normalScore;

    private Integer sensitiveScore;

    private Integer dehydratedScore;

    private String resultDescription;
}