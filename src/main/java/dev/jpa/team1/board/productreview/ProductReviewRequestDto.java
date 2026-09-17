package dev.jpa.team1.board.productreview;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequestDto {
    @NotNull
    private Long memberId;

    /**
     * 구매한 주문 상세를 기준으로 리뷰를 작성한다.
     */
    @NotNull
    private Long orderDetailId;

    /**
     * 하위 호환용 필드.
     * 실제 상품은 서버에서 ORDER_DETAIL -> PRODUCT_OPTION -> PRODUCT로 결정한다.
     */
    private Long productId;

    private Long beautySaveId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
