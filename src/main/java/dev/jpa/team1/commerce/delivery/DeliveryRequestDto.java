package dev.jpa.team1.commerce.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestDto {
    @NotBlank
    @Size(max = 50)
    private String recipient;

    @NotBlank
    @Size(max = 20)
    private String tel;

    @NotBlank
    @Size(max = 10)
    private String zipcode;

    @NotBlank
    @Size(max = 255)
    private String address1;

    @NotBlank
    @Size(max = 255)
    private String address2;

    @Size(max = 255)
    private String request;
}
