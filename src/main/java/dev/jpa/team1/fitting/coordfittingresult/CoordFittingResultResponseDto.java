package dev.jpa.team1.fitting.coordfittingresult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordFittingResultResponseDto {
    private Long id;

    private Long coordFittingId;

    private String fileOrigin;

    private String fileSaved;

    private String filePath;

    private String modelFilePath;

    private String aiDescription;

    private String success;

    private LocalDateTime createdAt;


    public static CoordFittingResultResponseDto from(CoordFittingResult result) {
        return new CoordFittingResultResponseDto(
                result.getId(),
                result.getCoordFitting().getId(),
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
