package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.member.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    @SequenceGenerator(name = "orders_seq", sequenceName = "orders_seq", allocationSize = 1)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "order_name", nullable = false, length = 255)
    private String orderName;

    @Column(name = "product_total", nullable = false)
    private Integer productTotal;

    @Column(name = "delivery_fee", nullable = false)
    private Integer deliveryFee;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false, length = 50)
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    public void updateStatus(String status) {
        this.status = status;
    }

    public void requestCancel() {
        this.status = "CANCEL_REQUESTED";
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.cancelDate = LocalDateTime.now();
    }
}
