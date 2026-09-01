package dev.jpa.team1.product.product;

import dev.jpa.team1.product.productcolor.ProductColorResponseDto;
import dev.jpa.team1.product.productsize.ProductSizeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private Long categoryId;
    private Long parentCategoryId;
    private String categoryName;
    private String brand;
    private String name;
    private String description;
    private Integer price;
    private String discount;
    private String material;
    private String washing;
    private String fittingEnabled;
    private String status;
    private Integer viewCount;
    private List<ProductColorResponseDto> colors;
    private List<ProductSizeResponseDto> sizes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getCategory().getId(),
                product.getCategory().getParent() != null
                        ? product.getCategory().getParent().getId()
                        : null,
                product.getCategory().getName(),
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscount(),
                product.getMaterial(),
                product.getWashing(),
                product.getFittingEnabled(),
                product.getStatus(),
                product.getViewCount(),
                List.of(),
                List.of(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public static ProductResponseDto from(
            Product product,
            List<ProductColorResponseDto> colors,
            List<ProductSizeResponseDto> sizes
    ) {
        return new ProductResponseDto(
                product.getId(),
                product.getCategory().getId(),
                product.getCategory().getParent() != null
                        ? product.getCategory().getParent().getId()
                        : null,
                product.getCategory().getName(),
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscount(),
                product.getMaterial(),
                product.getWashing(),
                product.getFittingEnabled(),
                product.getStatus(),
                product.getViewCount(),
                colors,
                sizes,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
