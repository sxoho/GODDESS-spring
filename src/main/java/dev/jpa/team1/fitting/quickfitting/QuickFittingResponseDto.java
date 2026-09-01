package dev.jpa.team1.fitting.quickfitting;

import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResult;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuickFittingResponseDto {

    private Long quickFittingId;

    private Long memberId;

    private Long productId;

    private String status;

    private String errorMessage;

    private LocalDateTime createdAt;

    // AI 합성 결과 정보 (SUCCESS 상태일 때 존재)
    private Long quickFittingResultId;

    private String fileSaved;

    private String filePath;

    private String aiDescription;

    private String success;

    public static QuickFittingResponseDto from(QuickFitting fitting, QuickFittingResult result) {
        QuickFittingResponseDtoBuilder builder = QuickFittingResponseDto.builder()
                .quickFittingId(fitting.getId())
                .memberId(fitting.getMember().getId())
                .productId(fitting.getProduct().getId())
                .status(fitting.getStatus())
                .errorMessage(fitting.getErrorMessage())
                .createdAt(fitting.getCreatedAt());

        if (result != null) {
            builder.quickFittingResultId(result.getId())
                    .fileSaved(result.getFileSaved())
                    .filePath(result.getFilePath())
                    .aiDescription(result.getAiDescription())
                    .success(result.getSuccess());
        }

        return builder.build();
    }
}