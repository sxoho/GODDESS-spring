package dev.jpa.team1.commerce.orders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuyNowItemRequestDto {

    @NotNull(message = "상품 옵션을 선택해주세요.")
    private Long productOptionId;

    @NotNull(message = "주문 수량을 입력해주세요.")
    @Min(value = 1, message = "주문 수량은 1개 이상이어야 합니다.")
    private Integer quantity;
}
