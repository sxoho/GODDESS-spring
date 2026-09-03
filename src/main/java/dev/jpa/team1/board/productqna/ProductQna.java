package dev.jpa.team1.board.productqna;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.product.product.Product;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_qna")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductQna {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_qna_seq"
    )
    @SequenceGenerator(
            name = "product_qna_seq",
            sequenceName = "product_qna_seq",
            allocationSize = 1
    )
    @Column(name = "productqna_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member member;

    @Column(
            nullable = false,
            length = 255
    )
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(
            nullable = false,
            length = 1
    )
    private String secret;

    @Lob
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Member answerer;

    @Column(
            nullable = false,
            length = 50
    )
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "answer_date")
    private LocalDateTime answerDate;

    /**
     * 문의 수정
     *
     * 답변 대기 상태에서만 Service에서 호출한다.
     */
    public void update(
            String title,
            String content,
            String secret
    ) {
        this.title = title;
        this.content = content;
        this.secret = secret;
    }

    /**
     * 관리자 답변 등록
     */
    public void answer(
            Member answerer,
            String answer
    ) {
        this.answerer = answerer;
        this.answer = answer;
        this.status = "ANSWERED";
        this.answerDate =
                LocalDateTime.now();
    }

    /**
     * 문의 종료 처리
     */
    public void close() {
        this.status = "CLOSED";
    }
}