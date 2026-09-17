package dev.jpa.team1.board.productreview;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_review_ai_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductReviewAiSummaryCache {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Lob
    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "review_count", nullable = false)
    private Long reviewCount;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
