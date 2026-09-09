package dev.jpa.team1.beauty.personalcolor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalColorResponseDto {

    private Long id;

    private Long memberId;

    private String personalColor;

    private Integer springScore;

    private Integer summerScore;

    private Integer autumnScore;

    private Integer winterScore;

    private String resultDescription;

    private LocalDateTime createdAt;


    public static PersonalColorResponseDto from(
            PersonalColorResult result
    ) {

        return new PersonalColorResponseDto(
                result.getId(),
                result.getMember().getId(),
                result.getPersonalColor(),
                result.getSpringScore(),
                result.getSummerScore(),
                result.getAutumnScore(),
                result.getWinterScore(),
                result.getResultDescription(),
                result.getCreatedAt()
        );
    }
}
