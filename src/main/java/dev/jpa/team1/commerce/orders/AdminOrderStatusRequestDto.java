package dev.jpa.team1.commerce.orders;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminOrderStatusRequestDto {

    @NotBlank
    private String status;

    private String company;
}
