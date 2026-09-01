package dev.jpa.team1.product.productcolorimage;

import dev.jpa.team1.product.productcolor.ProductColor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "product_color_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductColorImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "color_image_seq")
    @SequenceGenerator(name = "color_image_seq", sequenceName = "color_image_seq", allocationSize = 1)
    @Column(name = "color_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_color_id", nullable = false)
    private ProductColor productColor;

    @Column(name = "file_origin", nullable = false, length = 255)
    private String fileOrigin;

    @Column(name = "file_saved", nullable = false, length = 255)
    private String fileSaved;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "is_main", nullable = false, length = 1)
    private String isMain;

    @Column(name = "image_type", nullable = false, length = 50)
    private String imageType;

    @Column(nullable = false)
    private Integer sortOrder;

    public void updateMetadata(
            String isMain,
            String imageType,
            Integer sortOrder
    ) {
        this.isMain = isMain;
        this.imageType = imageType;
        this.sortOrder = sortOrder;
    }

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}