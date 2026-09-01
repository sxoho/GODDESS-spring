package dev.jpa.team1.board.customerqna;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerQnaRequestDto {

    @NotNull
    private Long memberId;

    @NotBlank
    @Size(max = 50)
    private String category;

    private Long orderId;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;
}