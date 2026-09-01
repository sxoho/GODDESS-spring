package dev.jpa.team1.commerce.orderdetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDto {
    private Long id;

    private Long orderId;

    private Long productOptionId;

    private Long quickFittingResultId;

    private Long coordFittingResultId;

    private String productName;

    private String colorName;

    private String sizeName;

    private Integer price;

    private Integer quantity;


    public static OrderDetailResponseDto from(OrderDetail detail) {
        return new OrderDetailResponseDto(
                detail.getId(),
                detail.getOrders().getId(),
                detail.getProductOption().getId(),
                detail.getQuickFittingResult() != null ? detail.getQuickFittingResult().getId() : null,
                detail.getCoordFittingResult() != null ? detail.getCoordFittingResult().getId() : null,
                detail.getProductName(),
                detail.getColorName(),
                detail.getSizeName(),
                detail.getPrice(),
                detail.getQuantity()
        );
    }

}
