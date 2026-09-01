package dev.jpa.team1.commerce.delivery;

import dev.jpa.team1.commerce.orders.Orders;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq")
    @SequenceGenerator(name = "delivery_seq", sequenceName = "delivery_seq", allocationSize = 1)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @Column(nullable = false, length = 50)
    private String recipient;

    @Column(nullable = false, length = 20)
    private String tel;

    @Column(nullable = false, length = 10)
    private String zipcode;

    @Column(nullable = false, length = 255)
    private String address1;

    @Column(nullable = false, length = 255)
    private String address2;

    @Column(length = 255)
    private String request;

    @Column(length = 50)
    private String company;

    @Column(name = "tracking_no", length = 100)
    private String trackingNo;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "complete_date")
    private LocalDateTime completeDate;

    public void updateTracking(
            @NotBlank(message = "택배사 이름은 필수 입력값입니다.") String company,
            @NotBlank(message = "운송장 번호는 필수 입력값입니다.") String trackingNo,
            String status) {

        this.company = company;
        this.trackingNo = trackingNo;
        this.status = status;
    }

    public void updateShippingDate(LocalDateTime date) {
        this.shippingDate = date;
    }
}
