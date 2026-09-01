package dev.jpa.team1.board.productreview;

import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.fitting.fittingsave.FittingSave;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.product.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_review_seq")
    @SequenceGenerator(name = "product_review_seq", sequenceName = "product_review_seq", allocationSize = 1)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 주문 기능 완성 전에는 NULL 허용. 추후 구매 검증 연결 시 다시 필수로 전환 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // AI 피팅 연동은 선택값
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitting_save_id")
    private FittingSave fittingSave;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 1)
    private String visible;

    public void update(
            String title,
            String content,
            Integer rating,
            FittingSave fittingSave
    ) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.fittingSave = fittingSave;
    }

    public void changeVisible(String visible) {
        this.visible = visible;
    }
}
