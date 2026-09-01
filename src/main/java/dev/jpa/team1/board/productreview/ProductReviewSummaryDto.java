package dev.jpa.team1.board.productreview;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewSummaryDto {
    private long reviewCount;
    private double averageRating;
}
