package dev.jpa.team1.commerce.delivery;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryTrackingUpdateRequestDto {

    @NotBlank(message = "택배사 이름은 필수 입력값입니다.")
    private String company;

    @NotBlank(message = "운송장 번호는 필수 입력값입니다.")
    private String trackingNo;
}