package dev.jpa.team1.product.productvariant;

import dev.jpa.team1.product.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_variant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_variant_seq")
    @SequenceGenerator(name = "product_variant_seq", sequenceName = "product_variant_seq", allocationSize = 1)
    @Column(name = "productvariant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "variant_name", nullable = false, length = 50)
    private String variantName;

    @Column(name = "variant_description", length = 255)
    private String variantDescription;

    @Builder.Default
    @Column(nullable = false, length = 1)
    private String visible = "Y";

    @Builder.Default
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    public void update(String variantName, String variantDescription, String visible, Integer sortOrder) {
        this.variantName = variantName;
        this.variantDescription = variantDescription;
        this.visible = visible;
        this.sortOrder = sortOrder == null ? 1 : sortOrder;
    }
}
