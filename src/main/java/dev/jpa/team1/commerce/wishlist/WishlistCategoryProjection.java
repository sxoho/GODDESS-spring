package dev.jpa.team1.commerce.wishlist;

public interface WishlistCategoryProjection {

    Long getCategoryId();

    String getCategoryName();

    Long getParentId();

    String getParentName();

    Long getProductCount();
}
