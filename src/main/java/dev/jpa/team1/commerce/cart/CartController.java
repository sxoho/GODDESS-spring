package dev.jpa.team1.commerce.cart;

import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commerce/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 담기
     * JWT의 subject(loginId)가 UserDetails.getUsername()으로 전달된다.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> addCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartRequestDto requestDto
    ) {
        CartResponseDto response = cartService.addCartItem(
                userDetails.getUsername(),
                requestDto
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 현재 로그인한 회원의 장바구니 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartResponseDto>>> getMyCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.getMyCart(userDetails.getUsername())
                )
        );
    }

    /**
     * 장바구니 수량 변경
     */
    @PutMapping("/{cartId}/quantity")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(name = "cartId") Long cartId,
            @Valid @RequestBody CartQuantityRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.updateQuantity(
                                userDetails.getUsername(),
                                cartId,
                                requestDto
                        )
                )
        );
    }

    /**
     * 장바구니 항목 삭제
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(name = "cartId") Long cartId
    ) {
        cartService.deleteCartItem(
                userDetails.getUsername(),
                cartId
        );

        return ResponseEntity.ok(
                ApiResponse.success("장바구니에서 상품이 삭제되었습니다.")
        );
    }
}
