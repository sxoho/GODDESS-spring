package dev.jpa.team1.product.productcolormaster;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_color_master")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductColorMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_color_master_seq")
    @SequenceGenerator(name = "product_color_master_seq", sequenceName = "product_color_master_seq", allocationSize = 1)
    @Column(name = "color_id")
    private Long id;
    @Column(name = "color_name", nullable = false, length = 50)
    private String colorName;
    @Column(name = "color_code", nullable = false, length = 20)
    private String colorCode;
    public void update(String colorName, String colorCode) { this.colorName=colorName; this.colorCode=colorCode; }
}
