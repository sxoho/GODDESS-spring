package dev.jpa.team1.commerce.cart;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CartRequestDto {
 @NotNull(message="상품 옵션 ID는 필수입니다.") private Long productOptionId;
 @Min(value=1,message="수량은 1개 이상이어야 합니다.") private int quantity=1;
}
