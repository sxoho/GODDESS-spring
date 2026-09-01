package dev.jpa.team1.fitting.coordfittingitem;

import dev.jpa.team1.fitting.coordfitting.CoordFitting;
import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productoption.ProductOption;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "coord_fitting_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoordFittingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coord_fitting_item_seq")
    @SequenceGenerator(name = "coord_fitting_item_seq", sequenceName = "coord_fitting_item_seq", allocationSize = 1)
    @Column(name = "coord_fitting_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coord_fitting_id", nullable = false)
    private CoordFitting coordFitting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_color_id")
    private ProductColor productColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @Column(name = "item_type", nullable = false, length = 50)
    private String itemType;

    @Column(nullable = false)
    private Integer sortOrder;
}
