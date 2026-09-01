package dev.jpa.team1.recommend.recommendproduct;

import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.recommend.stylerecommendation.StyleRecommendation;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "recommend_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RecommendProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recommend_product_seq")
    @SequenceGenerator(name = "recommend_product_seq", sequenceName = "recommend_product_seq", allocationSize = 1)
    @Column(name = "recommend_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private StyleRecommendation styleRecommendation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_color_id", nullable = false)
    private ProductColor productColor;

    @Column(name = "item_type", nullable = false, length = 50)
    private String itemType;

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(length = 255)
    private String reason;
}
