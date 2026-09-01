package dev.jpa.team1.board.productqna;

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
public class ProductQnaRequestDto {
    @NotNull
    private Long productId;

    @NotNull
    private Long memberId;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Size(max = 1)
    private String secret;
}
