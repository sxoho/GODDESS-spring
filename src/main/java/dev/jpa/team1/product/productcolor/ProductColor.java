package dev.jpa.team1.product.productcolor;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.productcolormaster.ProductColorMaster;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_color")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_color_seq")
    @SequenceGenerator(name = "product_color_seq", sequenceName = "product_color_seq", allocationSize = 1)
    @Column(name = "productcolor_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "color_id", nullable = false)
    private ProductColorMaster colorMaster;
    @Builder.Default @Column(name = "is_main", nullable = false, length = 1)
    private String isMain = "N";
    @Builder.Default @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
    @Builder.Default @Column(name = "ai_apply_enabled", nullable = false, length = 1)
    private String aiApplyEnabled = "Y";
    @Builder.Default @Column(nullable = false, length = 1)
    private String visible = "Y";

    public void update(ProductColorMaster colorMaster,String isMain,Integer sortOrder,String aiApplyEnabled,String visible){
        this.colorMaster=colorMaster; this.isMain=isMain; this.sortOrder=sortOrder==null?1:sortOrder;
        this.aiApplyEnabled=aiApplyEnabled==null?"Y":aiApplyEnabled; this.visible=visible==null?"Y":visible;
    }
}
