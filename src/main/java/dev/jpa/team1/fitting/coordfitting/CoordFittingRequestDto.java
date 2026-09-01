package dev.jpa.team1.fitting.coordfitting;

import dev.jpa.team1.fitting.coordfittingitem.CoordFittingItemDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;


@Getter
public class CoordFittingRequestDto {

    private Long memberId;

    private Long memberPhotoId;

    private Long fittingProfileId;

    @NotBlank(message = "피팅 방식(PHOTO/MODEL)은 필수값입니다.")
    private String fittingMethod;

    private Long topColorId;

    private Long bottomColorId;

    private Long outerColorId;

    @NotEmpty(message = "최소 하나 이상의 코디 아이템 구성이 필요합니다.")
    private List<CoordFittingItemDto> items;

}
