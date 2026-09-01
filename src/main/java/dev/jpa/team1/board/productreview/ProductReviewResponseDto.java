package dev.jpa.team1.board.productreview;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewResponseDto {

    private Long id;
    private Long memberId;
    private String memberNickname;
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private Long fittingSaveId;
    private String fittingImageUrl;
    private String title;
    private String content;
    private Integer rating;
    private String visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductReviewResponseDto from(ProductReview review) {
        return ProductReviewResponseDto.builder()
                .id(review.getId())
                .memberId(review.getMember().getId())
                .memberNickname(review.getMember().getNickname())
                .orderDetailId(review.getOrderDetail() != null ? review.getOrderDetail().getId() : null)
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getName())
                .fittingSaveId(review.getFittingSave() != null ? review.getFittingSave().getId() : null)
                .fittingImageUrl(getFittingImageUrl(review))
                .title(review.getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .visible(review.getVisible())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    private static String getFittingImageUrl(ProductReview review) {
        if (review.getFittingSave() == null) {
            return null;
        }

        if (review.getFittingSave().getQuickFittingResult() != null) {
            return review.getFittingSave().getQuickFittingResult().getFilePath();
        }

        if (review.getFittingSave().getCoordFittingResult() != null) {
            return review.getFittingSave().getCoordFittingResult().getFilePath();
        }

        return null;
    }
}
