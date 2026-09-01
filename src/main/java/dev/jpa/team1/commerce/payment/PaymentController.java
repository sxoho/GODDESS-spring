package dev.jpa.team1.commerce.payment;

import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commerce/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ==========================================
    // JWT 미사용
    // ==========================================
    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> approvePayment(
            @Valid @RequestBody PaymentApprovalRequestDto requestDto) {

        // 테스트를 위해 DB에 존재하는 임의의 회원 loginId를 하드코딩으로 전달합니다.
        String loginId = "testUser";

        PaymentResponseDto response = paymentService.processPayment(loginId, requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // ==========================================
    // JWT 사용
    // ==========================================
    /*
    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> approvePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PaymentApprovalRequestDto requestDto) {

        PaymentResponseDto response = paymentService.processPayment(userDetails.getUsername(), requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
    */
}