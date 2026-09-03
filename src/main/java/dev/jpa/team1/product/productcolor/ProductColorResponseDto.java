package dev.jpa.team1.product.productcolor;
import dev.jpa.team1.product.productcolorimage.ProductColorImageResponseDto;
import lombok.*;
import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductColorResponseDto {
    private Long id; private Long productId; private Long colorId; private String colorName; private String colorCode;
    private String isMain; private Integer sortOrder; private String aiApplyEnabled; private String visible;
    private List<ProductColorImageResponseDto> images;
    public static ProductColorResponseDto from(ProductColor c){ return from(c,List.of()); }
    public static ProductColorResponseDto from(ProductColor c,List<ProductColorImageResponseDto> images){
        return new ProductColorResponseDto(c.getId(),c.getProduct().getId(),c.getColorMaster().getId(),c.getColorMaster().getColorName(),c.getColorMaster().getColorCode(),c.getIsMain(),c.getSortOrder(),c.getAiApplyEnabled(),c.getVisible(),images);
    }
}
