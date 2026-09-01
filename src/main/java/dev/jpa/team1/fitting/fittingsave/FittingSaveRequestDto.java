package dev.jpa.team1.fitting.fittingsave;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FittingSaveRequestDto {

    @NotNull
    private Long memberId;

    @NotBlank(message = "피팅 종류(QUICK/COORD)를 지정해주세요.")
    private String fittingType;

    private Long quickFittingResultId;

    private Long coordFittingResultId;

    private String saveName;

    private String memo;
}
