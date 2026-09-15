package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commerce/orders")
@RequiredArgsConstructor
public class OrderCancelController {

    private final OrderCancelService orderCancelService;

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("orderId") Long orderId
    ) {

        String status = orderCancelService.cancelOrRequest(
                userDetails.getUsername(),
                orderId
        );

        return ResponseEntity.ok(
                ApiResponse.success(status)
        );
    }
}
