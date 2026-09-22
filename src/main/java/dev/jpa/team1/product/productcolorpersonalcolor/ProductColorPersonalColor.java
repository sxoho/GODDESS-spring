package dev.jpa.team1.product.productcolorpersonalcolor;

import dev.jpa.team1.product.productcolor.ProductColor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_color_personal_color",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_pcpc_color_type",
                columnNames = {"productcolor_id", "personal_color_type"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductColorPersonalColor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcpc_seq")
    @SequenceGenerator(
            name = "pcpc_seq",
            sequenceName = "pcpc_seq",
            allocationSize = 1
    )
    @Column(name = "pcpc_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productcolor_id", nullable = false)
    private ProductColor productColor;

    @Column(name = "personal_color_type", nullable = false, length = 20)
    private String personalColorType;
}
