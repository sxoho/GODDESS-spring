package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.orderdetail.OrderDetailRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotNull
    private Long memberId;

    @NotBlank
    @Size(max = 255)
    private String orderName;

    @NotNull
    private Integer productTotal;

    @NotNull
    private Integer deliveryFee;

    @NotNull
    private Integer discount;

    @NotNull
    private Integer total;

    @NotEmpty
    @Valid
    private List<OrderDetailRequestDto> orderDetails;
}
