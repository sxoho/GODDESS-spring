package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.delivery.DeliveryResponseDto;
import dev.jpa.team1.commerce.orderdetail.OrderDetailResponseDto;
import dev.jpa.team1.commerce.payment.PaymentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 상세 조회 응답 DTO
 *
 * 주문 기본정보 + 주문상품 + 결제정보 + 배송정보를
 * 한 번에 반환한다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDetailResponseDto {

    private Long id;
    private Long memberId;

    private String orderName;

    private Integer productTotal;
    private Integer deliveryFee;
    private Integer discount;
    private Integer total;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime cancelDate;

    private List<OrderDetailResponseDto> orderDetails;

    private PaymentResponseDto payment;

    private DeliveryResponseDto delivery;
}
