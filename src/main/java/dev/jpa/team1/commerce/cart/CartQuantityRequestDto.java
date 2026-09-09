package dev.jpa.team1.commerce.cart;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartQuantityRequestDto {

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;
}
