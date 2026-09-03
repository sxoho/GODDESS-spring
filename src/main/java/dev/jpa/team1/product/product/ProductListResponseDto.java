package dev.jpa.team1.product.product;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductListResponseDto {
 private Long id; private String name; private Integer price; private Integer discount; private String categoryName; private String mainImagePath; private String aiApplyEnabled; private String status;
 public static ProductListResponseDto from(Product p,String mainImagePath){return new ProductListResponseDto(p.getId(),p.getName(),p.getPrice(),p.getDiscount(),p.getCategory().getName(),mainImagePath,p.getAiApplyEnabled(),p.getStatus());}
}
