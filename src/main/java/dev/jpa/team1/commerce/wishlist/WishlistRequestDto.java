package dev.jpa.team1.commerce.wishlist;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequestDto {

    @NotNull
    private Long memberId;

    @NotNull
    private Long productId;
}
