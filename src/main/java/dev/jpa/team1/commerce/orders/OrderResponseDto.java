package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.orderdetail.OrderDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;

    private Long memberId;

    private String orderName;

    private Integer productTotal;

    private Integer deliveryFee;

    private Integer discount;

    private Integer total;

    private String status;

    private List<OrderDetailResponseDto> orderDetails;

    private LocalDateTime createdAt;

    private LocalDateTime cancelDate;


    public static OrderResponseDto from(Orders orders) {
        List<OrderDetailResponseDto> details = orders.getOrderDetails() != null
                ? orders.getOrderDetails().stream().map(OrderDetailResponseDto::from).toList()
                : List.of();

        return new OrderResponseDto(
                orders.getId(),
                orders.getMember().getId(),
                orders.getOrderName(),
                orders.getProductTotal(),
                orders.getDeliveryFee(),
                orders.getDiscount(),
                orders.getTotal(),
                orders.getStatus(),
                details,
                orders.getCreatedAt(),
                orders.getCancelDate()
        );
    }

}
