package dev.jpa.team1.board.productreview; import jakarta.validation.constraints.*; import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductReviewUpdateRequestDto {
 @NotNull private Long memberId; private Long beautySaveId; @NotBlank @Size(max=200) private String title; @NotBlank private String content; @NotNull @Min(1) @Max(5) private Integer rating;
}
