package dev.jpa.team1.beauty.personalcolor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonalColorRequestDto {

    private Long memberId;

    private String personalColor;

    private Integer springScore;

    private Integer summerScore;

    private Integer autumnScore;

    private Integer winterScore;

    private String resultDescription;
}