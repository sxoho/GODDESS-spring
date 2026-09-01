package dev.jpa.team1.board.productreview;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewVisibleRequestDto {

    @NotBlank
    @Pattern(regexp = "Y|N", message = "visible은 Y 또는 N만 가능합니다.")
    private String visible;
}
