package dev.jpa.team1.product.productoption;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductOptionResponseDto {
    private Long id; private Long productId; private Long productColorId; private String colorName; private Long productVariantId; private String variantName; private Integer addPrice; private Integer stock; private String visible;
    public static ProductOptionResponseDto from(ProductOption o){return new ProductOptionResponseDto(o.getId(),o.getProduct().getId(),o.getProductColor()!=null?o.getProductColor().getId():null,o.getProductColor()!=null?o.getProductColor().getColorMaster().getColorName():null,o.getProductVariant()!=null?o.getProductVariant().getId():null,o.getProductVariant()!=null?o.getProductVariant().getVariantName():null,o.getAddPrice(),o.getStock(),o.getVisible());}
}
