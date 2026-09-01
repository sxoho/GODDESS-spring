package dev.jpa.team1.fitting.coordfitting;

import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResult;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CoordFittingResponseDto {

    private Long coordFittingId;

    private Long memberId;

    private String fittingMethod;

    private String status;

    private String errorMessage;

    private LocalDateTime createdAt;

    // AI 합성 결과 정보
    private Long coordFittingResultId;

    private String fileSaved;

    private String filePath;

    private String aiDescription;

    private String success;

    public static CoordFittingResponseDto from(CoordFitting fitting, CoordFittingResult result) {
        CoordFittingResponseDtoBuilder builder = CoordFittingResponseDto.builder()
                .coordFittingId(fitting.getId())
                .memberId(fitting.getMember().getId())
                .fittingMethod(fitting.getFittingMethod())
                .status(fitting.getStatus())
                .errorMessage(fitting.getErrorMessage())
                .createdAt(fitting.getCreatedAt());

        if (result != null) {
            builder.coordFittingResultId(result.getId())
                    .fileSaved(result.getFileSaved())
                    .filePath(result.getFilePath())
                    .aiDescription(result.getAiDescription())
                    .success(result.getSuccess());
        }

        return builder.build();
    }
}