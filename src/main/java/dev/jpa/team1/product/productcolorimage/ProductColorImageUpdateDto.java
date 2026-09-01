package dev.jpa.team1.product.productcolorimage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductColorImageUpdateDto {
    private String isMain;
    private String imageType;
    private Integer sortOrder;
}
