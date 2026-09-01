package dev.jpa.team1.board.productreview;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequestDto {

    @NotNull(message = "회원 번호는 필수입니다.")
    private Long memberId;

    // 주문 기능 완성 후 필수값으로 전환 예정
    private Long orderDetailId;

    @NotNull(message = "상품 번호는 필수입니다.")
    private Long productId;

    // AI 피팅 결과 연결은 선택값
    private Long fittingSaveId;

    @NotBlank(message = "리뷰 제목을 입력해주세요.")
    @Size(max = 255, message = "리뷰 제목은 255자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    private String content;

    @NotNull(message = "별점을 선택해주세요.")
    @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5점 이하여야 합니다.")
    private Integer rating;
}
