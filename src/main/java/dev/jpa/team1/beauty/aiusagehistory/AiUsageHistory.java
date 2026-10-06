package dev.jpa.team1.beauty.aiusagehistory;


import dev.jpa.team1.member.member.Member;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "ai_usage_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AiUsageHistory {


    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ai_usage_history_seq"
    )
    @SequenceGenerator(
            name = "ai_usage_history_seq",
            sequenceName = "ai_usage_history_seq",
            allocationSize = 1
    )
    @Column(name = "ai_usage_history_id")
    private Long id;


    /**
     * AI 기능을 사용한 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member member;


    /**
     * AI 헤어 사용 종류
     *
     * HAIR_STYLE
     * HAIR_COLOR
     * HAIR_BOTH
     */
    @Column(
            name = "usage_type",
            nullable = false,
            length = 30
    )
    private String usageType;


    /**
     * 사용 일시
     */
    @CreationTimestamp
    @Column(
            name = "used_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime usedAt;
}