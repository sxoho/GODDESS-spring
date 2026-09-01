package dev.jpa.team1.product.product;

import dev.jpa.team1.product.category.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 255)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 50)
    private String discount;

    @Column(length = 255)
    private String material;

    @Column(length = 255)
    private String washing;

    @Column(name = "fitting_enabled", nullable = false, length = 1)
    private String fittingEnabled;

    @Column(nullable = false, length = 20)
    private String status;

    @Builder.Default
    private Integer viewCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public void update(
            Category category,
            String brand,
            String name,
            String description,
            Integer price,
            String discount,
            String material,
            String washing,
            String fittingEnabled,
            String status
    ) {
        this.category = category;
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.material = material;
        this.washing = washing;
        this.fittingEnabled = fittingEnabled;
        this.status = status;
    }

    public void stopSale() {
        this.status = "판매중지";
    }
    
    public void resumeSale() {
        this.status = "판매중";
    }
}
