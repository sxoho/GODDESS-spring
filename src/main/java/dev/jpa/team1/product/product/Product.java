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

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "short_description", length = 150)
    private String shortDescription;

    @Lob
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Builder.Default
    private Integer discount = 0;

    @Column(length = 100)
    private String brand;

    @Column(name = "usage_info", length = 500)
    private String usageInfo;

    @Builder.Default
    @Column(name = "ai_apply_enabled", nullable = false, length = 1)
    private String aiApplyEnabled = "Y";

    @Column(name = "product_type", length = 30)
    private String productType;

    @Column(length = 50)
    private String volume;

    @Column(name = "skin_type", length = 100)
    private String skinType;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String status = "ON_SALE";

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void update(Category category, String name, String shortDescription, String description, Integer price, Integer discount,
                       String brand, String usageInfo, String aiApplyEnabled, String productType,
                       String volume, String skinType, String status) {
        this.category = category;
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.price = price;
        this.discount = discount == null ? 0 : discount;
        this.brand = brand;
        this.usageInfo = usageInfo;
        this.aiApplyEnabled = aiApplyEnabled == null ? "Y" : aiApplyEnabled;
        this.productType = productType;
        this.volume = volume;
        this.skinType = skinType;
        this.status = status == null ? "ON_SALE" : status;
    }

    public void stopSale() { this.status = "STOPPED"; }
    public void resumeSale() { this.status = "ON_SALE"; }
}
