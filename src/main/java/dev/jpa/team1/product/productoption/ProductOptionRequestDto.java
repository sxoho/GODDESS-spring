package dev.jpa.team1.product.productoption;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductOptionRequestDto { private Long productColorId; private Long productVariantId; private Integer addPrice; private Integer stock; private String visible="Y"; }
