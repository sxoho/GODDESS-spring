package dev.jpa.team1.commerce.cart;
import lombok.*;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CartResponseDto {
 private Long id; private Long productOptionId; private String brand; private String productName; private String colorName; private String variantName; private Integer price; private Integer discount; private Integer addPrice; private Integer quantity; private LocalDateTime createdAt;
 public static CartResponseDto from(Cart c){var o=c.getProductOption(); return new CartResponseDto(c.getId(),o.getId(),o.getProduct().getBrand(),o.getProduct().getName(),o.getProductColor()!=null?o.getProductColor().getColorMaster().getColorName():null,o.getProductVariant()!=null?o.getProductVariant().getVariantName():null,o.getProduct().getPrice(),o.getProduct().getDiscount(),o.getAddPrice(),c.getQuantity(),c.getCreatedAt());}
}
