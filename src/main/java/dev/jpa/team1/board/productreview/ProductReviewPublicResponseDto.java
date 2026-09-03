package dev.jpa.team1.board.productreview; import lombok.*; import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductReviewPublicResponseDto {
 private Long id; private Long memberId; private String memberNickname; private Long productId; private String productName; private Long beautySaveId; private String beautyImageUrl; private String title; private String content; private Integer rating; private LocalDateTime createdAt; private LocalDateTime updatedAt;
 public static ProductReviewPublicResponseDto from(ProductReview r){ProductReviewResponseDto x=ProductReviewResponseDto.from(r);return builder().id(x.getId()).memberId(x.getMemberId()).memberNickname(mask(x.getMemberNickname())).productId(x.getProductId()).productName(x.getProductName()).beautySaveId(x.getBeautySaveId()).beautyImageUrl(x.getBeautyImageUrl()).title(x.getTitle()).content(x.getContent()).rating(x.getRating()).createdAt(x.getCreatedAt()).updatedAt(x.getUpdatedAt()).build();}
 private static String mask(String s){if(s==null||s.isBlank())return "-";if(s.length()==1)return s;return s.substring(0,1)+"*".repeat(s.length()-1);}
}
