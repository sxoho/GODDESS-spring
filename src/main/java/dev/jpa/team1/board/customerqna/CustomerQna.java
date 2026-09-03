package dev.jpa.team1.board.customerqna;

import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.member.member.Member;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_qna")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerQna {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_qna_seq"
    )
    @SequenceGenerator(
            name = "customer_qna_seq",
            sequenceName = "customer_qna_seq",
            allocationSize = 1
    )
    @Column(name = "customerqna_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member member;


    @Column(
            name = "category",
            nullable = false,
            length = 50
    )
    private String category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;


    @Column(
            name = "title",
            nullable = false,
            length = 255
    )
    private String title;


    @Lob
    @Column(
            name = "content",
            nullable = false
    )
    private String content;


    @Lob
    @Column(name = "answer")
    private String answer;


    @Column(
            name = "status",
            nullable = false,
            length = 50
    )
    private String status;


    @CreationTimestamp
    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(name = "answer_date")
    private LocalDateTime answerDate;


    /**
     * 고객 문의 수정
     * 답변 대기 상태에서만 Service에서 호출
     */
    public void update(
            String category,
            Orders orders,
            String title,
            String content
    ) {

        this.category = category;
        this.orders = orders;
        this.title = title;
        this.content = content;
    }


    /**
     * 관리자 답변 등록 / 수정
     * 답변 등록 시 WAITING -> ANSWERED
     */
    public void answer(
            String answer
    ) {

        this.answer = answer;
        this.status = "ANSWERED";
        this.answerDate = LocalDateTime.now();
    }
}