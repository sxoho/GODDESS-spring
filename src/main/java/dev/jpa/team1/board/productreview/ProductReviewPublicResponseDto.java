package dev.jpa.team1.board.productreview;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewPublicResponseDto {

    private Long id;
    private Long memberId;
    private String memberNickname;
    private Long productId;
    private String productName;
    private Long fittingSaveId;
    private String fittingImageUrl;
    private String title;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductReviewPublicResponseDto from(ProductReview review) {
        ProductReviewResponseDto response = ProductReviewResponseDto.from(review);

        return ProductReviewPublicResponseDto.builder()
                .id(response.getId())
                .memberId(response.getMemberId())
                .memberNickname(maskNickname(response.getMemberNickname()))
                .productId(response.getProductId())
                .productName(response.getProductName())
                .fittingSaveId(response.getFittingSaveId())
                .fittingImageUrl(response.getFittingImageUrl())
                .title(response.getTitle())
                .content(response.getContent())
                .rating(response.getRating())
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .build();
    }

    private static String maskNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return "-";
        }

        if (nickname.length() == 1) {
            return nickname;
        }

        return nickname.substring(0, 1)
                + "*".repeat(nickname.length() - 1);
    }
}
