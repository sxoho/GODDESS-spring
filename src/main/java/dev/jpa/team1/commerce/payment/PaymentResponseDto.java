package dev.jpa.team1.commerce.payment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponseDto {
    private Long paymentId;
    private Long orderId;
    private String paymentMethod;
    private int amount;
    private String status;
    private String approvalNo;
    private LocalDateTime createdAt;

    public static PaymentResponseDto from(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrders().getId())
                .paymentMethod(payment.getPaymentMethod())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .approvalNo(payment.getApprovalNo())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}