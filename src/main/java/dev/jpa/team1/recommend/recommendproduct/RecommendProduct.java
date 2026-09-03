package dev.jpa.team1.recommend.recommendproduct;
import dev.jpa.team1.beauty.recommendation.BeautyRecommendation;
import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.productcolor.ProductColor;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="recommend_product") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class RecommendProduct {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="recommend_product_seq") @SequenceGenerator(name="recommend_product_seq",sequenceName="recommend_product_seq",allocationSize=1) @Column(name="recommendproduct_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="recommendation_id",nullable=false) private BeautyRecommendation beautyRecommendation;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id",nullable=false) private Product product;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="productcolor_id") private ProductColor productColor;
 @Column(name="item_type",length=30) private String itemType; @Builder.Default @Column(name="sort_order",nullable=false) private Integer sortOrder=1; @Column(length=500) private String reason;
}
