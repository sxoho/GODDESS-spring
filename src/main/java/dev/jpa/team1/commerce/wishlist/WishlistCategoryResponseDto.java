package dev.jpa.team1.commerce.wishlist;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistCategoryResponseDto {

    private Long categoryId;
    private String categoryName;

    private Long parentId;
    private String parentName;

    private Long productCount;

    public static WishlistCategoryResponseDto from(
            WishlistCategoryProjection projection
    ) {
        return new WishlistCategoryResponseDto(
                projection.getCategoryId(),
                projection.getCategoryName(),
                projection.getParentId(),
                projection.getParentName(),
                projection.getProductCount()
        );
    }
}
