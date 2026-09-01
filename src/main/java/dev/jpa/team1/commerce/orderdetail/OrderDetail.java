package dev.jpa.team1.commerce.orderdetail;

import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResult;
import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResult;
import dev.jpa.team1.product.productoption.ProductOption;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "order_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_seq")
    @SequenceGenerator(name = "order_detail_seq", sequenceName = "order_detail_seq", allocationSize = 1)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quick_fitting_result_id")
    private QuickFittingResult quickFittingResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coord_fitting_result_id")
    private CoordFittingResult coordFittingResult;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "color_name", nullable = false, length = 50)
    private String colorName;

    @Column(name = "size_name", nullable = false, length = 50)
    private String sizeName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;
}
