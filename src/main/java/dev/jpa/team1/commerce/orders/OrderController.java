package dev.jpa.team1.commerce.orders;

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
@RequestMapping("/api/commerce/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService ordersService;

    /**
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCreateRequestDto requestDto) {

        OrderResponseDto response = ordersService.createOrder(
                userDetails.getUsername(),
                requestDto
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 로그인 회원의 주문내역 목록
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<OrderResponseDto> response =
                ordersService.getMyOrders(
                        userDetails.getUsername()
                );

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }

    /**
     * 로그인 회원의 주문 상세
     *
     * ORDERS
     * + ORDER_DETAIL
     * + PAYMENT
     * + DELIVERY
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderHistoryDetailResponseDto>> getMyOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("orderId") Long orderId) {

        OrderHistoryDetailResponseDto response =
                ordersService.getMyOrder(
                        userDetails.getUsername(),
                        orderId
                );

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }
}
