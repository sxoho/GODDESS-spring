package dev.jpa.team1.commerce.cart;

import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commerce/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

//    // 장바구니 담기 API
//    @PostMapping
//    public ResponseEntity<ApiResponse<CartResponseDto>> addCartItem(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @Valid @RequestBody CartRequestDto requestDto) {
//
//        CartResponseDto response = cartService.addCartItem(userDetails.getUsername(), requestDto);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(ApiResponse.success(response));
//    }

    // 장바구니 담기 API (JWT 임시 비활성화)
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> addCartItemWithNoAuth(
            @RequestParam(name = "loginId") String loginId,
            @Valid @RequestBody CartRequestDto requestDto) {

        CartResponseDto response = cartService.addCartItem(loginId, requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 내 장바구니 리스트 조회 API
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<CartResponseDto>>> getMyCart(
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        List<CartResponseDto> response = cartService.getMyCart(userDetails.getUsername());
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }

    // 내 장바구니 리스트 조회 API (JWT 임시 비활성화)
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartResponseDto>>> getMyCartWithNoAuth(
            @RequestParam(name = "loginId") String loginId) {

        List<CartResponseDto> response = cartService.getMyCart(loginId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

//    // 장바구니 항목 삭제 API
//    @DeleteMapping("/{cartId}")
//    public ResponseEntity<ApiResponse<Void>> deleteCartItem(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @PathVariable Long cartId) {
//
//        cartService.deleteCartItem(userDetails.getUsername(), cartId);
//        return ResponseEntity.ok(ApiResponse.success("장바구니에서 상품이 삭제되었습니다."));
//    }

    // 장바구니 항목 삭제 API (JWT 임시 비활성화)
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItemWithNoAuth(
            @RequestParam(name = "loginId") String loginId,
            @PathVariable(name = "cartId") Long cartId) {

        cartService.deleteCartItem(loginId, cartId);
        return ResponseEntity.ok(ApiResponse.success("장바구니에서 상품이 삭제되었습니다."));
    }
}