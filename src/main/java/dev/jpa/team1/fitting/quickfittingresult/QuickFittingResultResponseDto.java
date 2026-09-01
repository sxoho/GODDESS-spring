package dev.jpa.team1.fitting.quickfittingresult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuickFittingResultResponseDto {
    private Long id;

    private Long quickFittingId;

    private Long productColorId;

    private String colorName;

    private String fileOrigin;

    private String fileSaved;

    private String filePath;

    private String modelFilePath;

    private String aiDescription;

    private String success;

    private LocalDateTime createdAt;


    public static QuickFittingResultResponseDto from(QuickFittingResult result) {
        return new QuickFittingResultResponseDto(
                result.getId(),
                result.getQuickFitting().getId(),
                result.getProductColor().getId(),
                result.getProductColor().getProductColorInfo().getColorName(),
                result.getFileOrigin(),
                result.getFileSaved(),
                result.getFilePath(),
                result.getModelFilePath(),
                result.getAiDescription(),
                result.getSuccess(),
                result.getCreatedAt()
        );
    }

}
