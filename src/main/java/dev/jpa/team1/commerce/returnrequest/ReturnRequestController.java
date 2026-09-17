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

    /** 사용자 반품 요청 */
    @PostMapping
    public ResponseEntity<ApiResponse<ReturnResponseDto>> createReturnRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReturnCreateRequestDto requestDto
    ) {
        ReturnResponseDto response = returnRequestService.createReturnRequest(
                userDetails.getUsername(),
                requestDto
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /** 사용자 본인 반품 요청 목록 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReturnResponseDto>>> getMyReturnRequests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        returnRequestService.getMyReturnRequests(userDetails.getUsername())
                )
        );
    }

    /** 관리자 반품 요청 전체 목록 */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<List<ReturnResponseDto>>> getAdminReturnRequests() {
        return ResponseEntity.ok(
                ApiResponse.success(returnRequestService.getAdminReturnRequests())
        );
    }

    /** 관리자 승인 / 거절 */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{returnId}/process")
    public ResponseEntity<ApiResponse<ReturnResponseDto>> processReturnRequest(
    		@PathVariable("returnId") Long returnId,
            @Valid @RequestBody ReturnProcessRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        returnRequestService.processReturnRequest(returnId, requestDto)
                )
        );
    }
}
