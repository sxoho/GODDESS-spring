package dev.jpa.team1.commerce.payment;

import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commerce/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> approvePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PaymentApprovalRequestDto requestDto) {

        PaymentResponseDto response = paymentService.processPayment(
                userDetails.getUsername(),
                requestDto
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
