package dev.jpa.team1.commerce.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentApprovalRequestDto {

    @NotNull(message = "주문 번호는 필수입니다.")
    private Long orderId;

    @NotBlank(message = "결제 수단을 입력해주세요. (예: CARD, TRANSFER)")
    private String paymentMethod;

    @NotNull(message = "결제 승인 금액은 필수입니다.")
    private Integer amount;

    @NotBlank(message = "PG사 결제 승인 번호(imp_uid 등)는 필수입니다.")
    private String approvalNo;
}