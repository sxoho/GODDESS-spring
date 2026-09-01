package dev.jpa.team1.product.productcolor;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.productcolorinfo.ProductColorInfo;
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
    @Column(name = "product_color_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private ProductColorInfo productColorInfo;

    @Column(name = "is_main", nullable = false, length = 1)
    private String isMain;

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(name = "fitting_enabled", nullable = false, length = 1)
    private String fittingEnabled;

    @Column(nullable = false, length = 1)
    private String visible;

    public void update(
            ProductColorInfo productColorInfo,
            String isMain,
            Integer sortOrder,
            String fittingEnabled,
            String visible
    ) {
        this.productColorInfo = productColorInfo;
        this.isMain = isMain;
        this.sortOrder = sortOrder;
        this.fittingEnabled = fittingEnabled;
        this.visible = visible;
    }
}