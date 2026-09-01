package dev.jpa.team1.commerce.returnrequest;

import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commerce/returns")
@RequiredArgsConstructor
public class ReturnRequestController {

    private final ReturnRequestService returnRequestService;

    // 반품/환불 신청 API (일반 사용자)
    @PostMapping
    public ResponseEntity<ApiResponse<ReturnResponseDto>> createReturnRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReturnCreateRequestDto requestDto) {

        ReturnResponseDto response = returnRequestService.createReturnRequest(userDetails.getUsername(), requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 내 반품 내역 조회 API (일반 사용자)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReturnResponseDto>>> getMyReturnRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<ReturnResponseDto> response = returnRequestService.getMyReturnRequests(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품/환불 처리 확정 API (관리자 전용)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{returnId}/process")
    public ResponseEntity<ApiResponse<ReturnResponseDto>> processReturnRequest(
            @PathVariable Long returnId,
            @Valid @RequestBody ReturnProcessRequestDto requestDto) {

        ReturnResponseDto response = returnRequestService.processReturnRequest(returnId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}