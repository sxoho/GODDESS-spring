package dev.jpa.team1.commerce.returnrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReturnProcessRequestDto {

    @NotBlank(message = "처리 상태(COMPLETED, REJECTED 등)를 지정해주세요.")
    private String status;

    @NotNull(message = "환불 금액을 입력해주세요.")
    private Integer refundAmount;
}