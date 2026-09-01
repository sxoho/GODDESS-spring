package dev.jpa.team1.product.productoption;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productsize.ProductSize;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "product_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_option_seq")
    @SequenceGenerator(name = "product_option_seq", sequenceName = "product_option_seq", allocationSize = 1)
    @Column(name = "product_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_color_id", nullable = false)
    private ProductColor productColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_size_id", nullable = false)
    private ProductSize productSize;

    @Column(name = "add_price")
    @Builder.Default
    private Integer addPrice = 0;

    @Builder.Default
    private Integer stock = 0;

    @Column(nullable = false, length = 1)
    private String visible;

    public void update(
            ProductColor productColor,
            ProductSize productSize,
            Integer addPrice,
            Integer stock,
            String visible
    ) {
        this.productColor = productColor;
        this.productSize = productSize;
        this.addPrice = addPrice == null ? 0 : addPrice;
        this.stock = stock == null ? 0 : stock;
        this.visible = visible;
    }

    // 주문 시 재고 차감 로직
    public void decreaseStock(Integer quantity) {
        if (this.stock < quantity) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }
        this.stock -= quantity;
    }
}