package dev.jpa.team1.product.productcolorimage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorImageResponseDto {
    private Long id;

    private Long productColorId;

    private String fileOrigin;

    private String fileSaved;

    private String filePath;

    private String isMain;

    private String imageType;

    private Integer sortOrder;

    private LocalDateTime createdAt;


    public static ProductColorImageResponseDto from(ProductColorImage image) {
        return new ProductColorImageResponseDto(
                image.getId(),
                image.getProductColor().getId(),
                image.getFileOrigin(),
                image.getFileSaved(),
                image.getFilePath(),
                image.getIsMain(),
                image.getImageType(),
                image.getSortOrder(),
                image.getCreatedAt()
        );
    }

}
