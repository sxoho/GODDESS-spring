package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.delivery.Delivery;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminOrderResponseDto {
    private Long id;
    private String loginId;
    private String memberName;
    private String orderName;
    private Integer total;
    private String status;
    private LocalDateTime createdAt;

    private Long deliveryId;
    private String deliveryStatus;
    private String company;
    private String trackingNo;
    private LocalDateTime shippingDate;
    private LocalDateTime completeDate;

    public static AdminOrderResponseDto from(Orders order, Delivery delivery) {
        return AdminOrderResponseDto.builder()
                .id(order.getId())
                .loginId(order.getMember().getLoginId())
                .memberName(order.getMember().getName())
                .orderName(order.getOrderName())
                .total(order.getTotal())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .deliveryId(delivery != null ? delivery.getId() : null)
                .deliveryStatus(delivery != null ? delivery.getStatus() : null)
                .company(delivery != null ? delivery.getCompany() : null)
                .trackingNo(delivery != null ? delivery.getTrackingNo() : null)
                .shippingDate(delivery != null ? delivery.getShippingDate() : null)
                .completeDate(delivery != null ? delivery.getCompleteDate() : null)
                .build();
    }
}
