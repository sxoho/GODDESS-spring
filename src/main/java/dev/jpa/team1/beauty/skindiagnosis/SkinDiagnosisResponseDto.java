package dev.jpa.team1.beauty.skindiagnosis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SkinDiagnosisResponseDto {

    private Long id;

    private Long memberId;

    private String skinType;

    private Integer dryScore;

    private Integer oilyScore;

    private Integer combinationScore;

    private Integer normalScore;

    private Integer sensitiveScore;

    private Integer dehydratedScore;

    private String resultDescription;

    private LocalDateTime createdAt;

    public static SkinDiagnosisResponseDto from(
            SkinDiagnosisResult result
    ) {

        return new SkinDiagnosisResponseDto(
                result.getId(),
                result.getMember().getId(),
                result.getSkinType(),
                result.getDryScore(),
                result.getOilyScore(),
                result.getCombinationScore(),
                result.getNormalScore(),
                result.getSensitiveScore(),
                result.getDehydratedScore(),
                result.getResultDescription(),
                result.getCreatedAt()
        );
    }
}