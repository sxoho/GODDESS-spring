package dev.jpa.team1.commerce.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private Long id;

    private Long productOptionId;

    private String brand;

    private String productName;

    private String colorName;

    private String sizeName;

    private Integer price;

    private String discount;

    private Integer addPrice;

    private Integer quantity;

    private String filePath;

    private Long quickFittingResultId;

    private Long coordFittingResultId;

    private LocalDateTime createdAt;


    public static CartResponseDto from(Cart cart) {
        return new CartResponseDto(
                cart.getId(),
                cart.getProductOption().getId(),
                cart.getProductOption().getProduct() != null ? cart.getProductOption().getProduct().getBrand() : null,
                cart.getProductOption().getProduct() != null ? cart.getProductOption().getProduct().getName() : null,
                cart.getProductOption().getProductColor() != null && cart.getProductOption().getProductColor().getProductColorInfo() != null 
                        ? cart.getProductOption().getProductColor().getProductColorInfo().getColorName() : null,
                cart.getProductOption().getProductSize() != null ? cart.getProductOption().getProductSize().getSizeName() : null,
                cart.getProductOption().getProduct() != null ? cart.getProductOption().getProduct().getPrice() : 0,
                cart.getProductOption().getProduct() != null ? cart.getProductOption().getProduct().getDiscount() : null,
                cart.getProductOption().getAddPrice(),
                cart.getQuantity(),
                cart.getFilePath(),
                cart.getQuickFittingResult() != null ? cart.getQuickFittingResult().getId() : null,
                cart.getCoordFittingResult() != null ? cart.getCoordFittingResult().getId() : null,
                cart.getCreatedAt()
        );
    }

}
