package dev.jpa.team1.commerce.returnrequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReturnProcessRequestDto {

    @NotBlank(message = "처리 상태(APPROVED 또는 REJECTED)를 지정해주세요.")
    private String status;

    /** 승인 시 기록할 예정 환불 금액. 거절 시에는 0으로 저장된다. */
    private Integer refundAmount;
}
