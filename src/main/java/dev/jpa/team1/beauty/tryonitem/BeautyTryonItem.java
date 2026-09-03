package dev.jpa.team1.beauty.tryonitem;
import dev.jpa.team1.beauty.tryon.BeautyTryon; import dev.jpa.team1.product.product.Product; import dev.jpa.team1.product.productcolor.ProductColor; import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="beauty_tryon_item") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class BeautyTryonItem {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="beauty_tryon_item_seq") @SequenceGenerator(name="beauty_tryon_item_seq",sequenceName="beauty_tryon_item_seq",allocationSize=1) @Column(name="beautytryonitem_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="beautytryon_id",nullable=false) private BeautyTryon beautyTryon; @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id") private Product product; @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="productcolor_id") private ProductColor productColor; @Column(name="item_type",nullable=false,length=30) private String itemType; @Column(name="style_value",length=100) private String styleValue; @Builder.Default @Column(name="sort_order",nullable=false) private Integer sortOrder=1;
}
