package dev.jpa.team1.board.productqna;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaAnswerRequestDto {

    @NotNull
    private Long answererId;

    @NotBlank
    private String answer;
}
