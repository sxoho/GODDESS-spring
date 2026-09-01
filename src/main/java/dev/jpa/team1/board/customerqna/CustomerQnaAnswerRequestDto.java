package dev.jpa.team1.board.customerqna;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerQnaAnswerRequestDto {

    @NotBlank
    private String answer;
}