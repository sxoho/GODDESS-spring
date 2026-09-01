package dev.jpa.team1.member.grade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeResponseDto {
    private Long id;

    private String name;

    private String description;

    private String visible;

    private LocalDateTime createdAt;


    public static GradeResponseDto from(Grade grade) {
        return new GradeResponseDto(
                grade.getId(),
                grade.getName(),
                grade.getDescription(),
                grade.getVisible(),
                grade.getCreatedAt()
        );
    }

}
