package dev.jpa.team1.product.productcolorinfo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_color_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductColorInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_color_info_seq")
    @SequenceGenerator(name = "product_color_info_seq", sequenceName = "product_color_info_seq", allocationSize = 1)
    @Column(name = "color_id")
    private Long id;

    @Column(name = "color_name", nullable = false, length = 50)
    private String colorName;

    @Column(name = "color_code", nullable = false, length = 20)
    private String colorCode;

}
