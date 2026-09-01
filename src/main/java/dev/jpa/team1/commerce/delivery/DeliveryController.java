package dev.jpa.team1.commerce.delivery;

import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commerce/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 배송 조회 API (일반 사용자)
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryResponseDto>> getDeliveryInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {

        DeliveryResponseDto response = deliveryService.getMyDeliveryInfo(userDetails.getUsername(), orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 운송장 등록 및 배송 출발 API (관리자 전용)
    @PreAuthorize("hasRole('ADMIN')") // Security 설정에서 ADMIN 권한만 접근 허용
    @PatchMapping("/{deliveryId}/tracking")
    public ResponseEntity<ApiResponse<DeliveryResponseDto>> updateTrackingInfo(
            @PathVariable Long deliveryId,
            @Valid @RequestBody DeliveryTrackingUpdateRequestDto requestDto) {

        DeliveryResponseDto response = deliveryService.updateTrackingInfo(deliveryId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}