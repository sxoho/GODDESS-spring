package dev.jpa.team1.member.grade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeRequestDto {
    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 1)
    private String visible;
}
