package dev.jpa.team1.commerce.orderdetail;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderDetailResponseDto {
 private Long id; private Long orderId; private Long productOptionId; private String productName; private String colorName; private String variantName; private Integer price; private Integer quantity;
 public static OrderDetailResponseDto from(OrderDetail d){return new OrderDetailResponseDto(d.getId(),d.getOrders().getId(),d.getProductOption().getId(),d.getProductName(),d.getColorName(),d.getVariantName(),d.getPrice(),d.getQuantity());}
}
