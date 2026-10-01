package dev.jpa.team1.beauty.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class BeautySaveResponseDto {

    private boolean success;

    private String message;

    private Long beautySaveId;

    private Long beautyResultId;

    private String saveName;
}