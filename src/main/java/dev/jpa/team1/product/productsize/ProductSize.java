package dev.jpa.team1.product.productsize;

import dev.jpa.team1.product.product.Product;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "product_size")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_size_seq")
    @SequenceGenerator(name = "product_size_seq", sequenceName = "product_size_seq", allocationSize = 1)
    @Column(name = "product_size_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "size_name", nullable = false, length = 50)
    private String sizeName;

    @Column(name = "size_description", length = 255)
    private String sizeDescription;

    @Column(nullable = false, length = 1)
    private String visible;

    @Column(nullable = false)
    private Integer sortOrder;

    public void update(
            String sizeName,
            String sizeDescription,
            String visible,
            Integer sortOrder
    ) {
        this.sizeName = sizeName;
        this.sizeDescription = sizeDescription;
        this.visible = visible;
        this.sortOrder = sortOrder;
    }
}