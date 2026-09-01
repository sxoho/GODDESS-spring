package dev.jpa.team1.fitting.fittingsave;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FittingSaveResponseDto {

    private Long fittingSaveId;

    private Long memberId;

    private String fittingType;

    private Long quickFittingResultId;

    private Long coordFittingResultId;

    private String saveName;

    private String memo;

    private LocalDateTime createdAt;

    public static FittingSaveResponseDto from(FittingSave save) {
        return FittingSaveResponseDto.builder()
                .fittingSaveId(save.getId())
                .memberId(save.getMember().getId())
                .fittingType(save.getFittingType())
                .quickFittingResultId(save.getQuickFittingResult() != null ? save.getQuickFittingResult().getId() : null)
                .coordFittingResultId(save.getCoordFittingResult() != null ? save.getCoordFittingResult().getId() : null)
                .saveName(save.getSaveName())
                .memo(save.getMemo())
                .createdAt(save.getCreatedAt())
                .build();
    }
}