package dev.jpa.team1.commerce.returnrequest;

import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.member.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "return_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReturnRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_request_seq")
    @SequenceGenerator(name = "return_request_seq", sequenceName = "return_seq", allocationSize = 1)
    @Column(name = "return_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderdetail_id", nullable = false)
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "reason_type", nullable = false, length = 50)
    private String reasonType;

    @Lob
    private String reason;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "process_date")
    private LocalDateTime processDate;

    @Column(name = "refund_amount")
    private Integer refundAmount;

    public void updateProcess(
            @NotBlank(message = "처리 상태(COMPLETED, REJECTED 등)를 지정해주세요.") String status,
            @NotNull(message = "환불 금액을 입력해주세요.") Integer refundAmount,
            LocalDateTime now) {

        this.status = status;
        this.refundAmount = refundAmount;
        this.processDate = now;
    }
}
