package dev.jpa.team1.commerce.wishlist;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponseDto {

    private Long id;
    private Long productId;
    private Long categoryId;

    private String brand;
    private String productName;

    private Integer price;
    private Integer discount;

    private LocalDateTime createdAt;

    public static WishlistResponseDto from(
            Wishlist wishlist
    ) {
        return new WishlistResponseDto(
                wishlist.getId(),
                wishlist.getProduct().getId(),
                wishlist.getProduct()
                        .getCategory()
                        .getId(),
                wishlist.getProduct().getBrand(),
                wishlist.getProduct().getName(),
                wishlist.getProduct().getPrice(),
                wishlist.getProduct().getDiscount(),
                wishlist.getCreatedAt()
        );
    }
}