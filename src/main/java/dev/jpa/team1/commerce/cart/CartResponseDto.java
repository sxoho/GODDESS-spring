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
    private Long productId;

    private String brand;
    private String productName;
    private String colorName;
    private String variantName;

    private Integer price;
    private Integer discount;
    private Integer addPrice;
    private Integer stock;
    private Integer quantity;

    private String filePath;
    private String productStatus;
    private String optionVisible;

    private LocalDateTime createdAt;

    public static CartResponseDto from(Cart cart, String filePath) {
        var option = cart.getProductOption();
        var product = option.getProduct();

        return new CartResponseDto(
                cart.getId(),
                option.getId(),
                product.getId(),
                product.getBrand(),
                product.getName(),
                option.getProductColor() != null
                        ? option.getProductColor().getColorMaster().getColorName()
                        : null,
                option.getProductVariant() != null
                        ? option.getProductVariant().getVariantName()
                        : null,
                product.getPrice(),
                product.getDiscount(),
                option.getAddPrice(),
                option.getStock(),
                cart.getQuantity(),
                filePath,
                product.getStatus(),
                option.getVisible(),
                cart.getCreatedAt()
        );
    }
}
