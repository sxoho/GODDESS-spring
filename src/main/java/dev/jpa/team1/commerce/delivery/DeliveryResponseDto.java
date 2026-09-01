package dev.jpa.team1.commerce.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
    private Long id;

    private Long orderId;

    private String recipient;

    private String tel;

    private String zipcode;

    private String address1;

    private String address2;

    private String request;

    private String company;

    private String trackingNo;

    private String status;

    private LocalDateTime shippingDate;

    private LocalDateTime completeDate;


    public static DeliveryResponseDto from(Delivery delivery) {
        return new DeliveryResponseDto(
                delivery.getId(),
                delivery.getOrders().getId(),
                delivery.getRecipient(),
                delivery.getTel(),
                delivery.getZipcode(),
                delivery.getAddress1(),
                delivery.getAddress2(),
                delivery.getRequest(),
                delivery.getCompany(),
                delivery.getTrackingNo(),
                delivery.getStatus(),
                delivery.getShippingDate(),
                delivery.getCompleteDate()
        );
    }

}
