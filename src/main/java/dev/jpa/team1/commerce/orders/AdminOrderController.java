package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commerce/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminOrderResponseDto>>> findAll(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminOrderService.findAll(
                                userDetails.getUsername()
                        )
                )
        );
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody AdminOrderStatusRequestDto requestDto
    ) {

    	adminOrderService.updateStatus(
    	        userDetails.getUsername(),
    	        orderId,
    	        requestDto
    	);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "주문 상태가 변경되었습니다."
                )
        );
    }

    @PatchMapping("/{orderId}/cancel/approve")
    public ResponseEntity<ApiResponse<String>> approveCancel(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("orderId") Long orderId
    ) {

        adminOrderService.approveCancelRequest(
                userDetails.getUsername(),
                orderId
        );

        return ResponseEntity.ok(
                ApiResponse.success("취소 요청이 승인되었습니다.")
        );
    }

    @PatchMapping("/{orderId}/cancel/reject")
    public ResponseEntity<ApiResponse<String>> rejectCancel(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("orderId") Long orderId
    ) {

        adminOrderService.rejectCancelRequest(
                userDetails.getUsername(),
                orderId
        );

        return ResponseEntity.ok(
                ApiResponse.success("취소 요청이 거절되었습니다.")
        );
    }
}
