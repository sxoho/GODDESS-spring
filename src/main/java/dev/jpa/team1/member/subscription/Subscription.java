package dev.jpa.team1.member.subscription;


import dev.jpa.team1.member.member.Member;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Subscription {


    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subscription_seq"
    )
    @SequenceGenerator(
            name = "subscription_seq",
            sequenceName = "subscription_seq",
            allocationSize = 1
    )
    @Column(name = "subscription_id")
    private Long id;


    /**
     * 구독 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member member;


    /**
     * 구독 플랜
     *
     * MONTHLY
     * YEARLY
     */
    @Column(
            name = "plan_type",
            nullable = false,
            length = 20
    )
    private String planType;


    /**
     * 구독 상태
     *
     * ACTIVE
     * EXPIRED
     * CANCELLED
     */
    @Column(
            nullable = false,
            length = 20
    )
    private String status;


    /**
     * 결제 금액
     *
     * MONTHLY : 5000
     * YEARLY  : 40000
     */
    @Column(nullable = false)
    private Long price;


    /**
     * 구독 시작일
     */
    @Column(
            name = "start_date",
            nullable = false
    )
    private LocalDateTime startDate;


    /**
     * 구독 종료일
     */
    @Column(
            name = "end_date",
            nullable = false
    )
    private LocalDateTime endDate;


    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    /**
     * 구독 취소
     */
    public void cancel() {

        this.status =
                "CANCELLED";

        this.updatedAt =
                LocalDateTime.now();
    }


    /**
     * 구독 만료 처리
     */
    public void expire() {

        this.status =
                "EXPIRED";

        this.updatedAt =
                LocalDateTime.now();
    }
}