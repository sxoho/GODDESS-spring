package dev.jpa.team1.commerce.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotBlank
    @Size(max = 50)
    private String paymentMethod;

    @NotNull
    private Integer amount;

    @NotBlank
    @Size(max = 50)
    private String status;

    @Size(max = 100)
    private String approvalNo;
}
