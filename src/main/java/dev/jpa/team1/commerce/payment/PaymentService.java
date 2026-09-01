package dev.jpa.team1.commerce.payment;

import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.commerce.orders.OrdersRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PaymentResponseDto processPayment(String loginId, PaymentApprovalRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Orders order = ordersRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 1. 권한 및 상태 검증
        if (!order.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE); // 권한 없음 예외로 처리
        }
        if (!"PENDING_PAYMENT".equals(order.getStatus())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "결제 대기 상태의 주문이 아닙니다.");
        }

        // 2. 결제 금액 변조 검증 (DB 주문 총액 vs 클라이언트 결제 요청 금액)
        if (!order.getTotal().equals(requestDto.getAmount())) {
            log.error("결제 금액 불일치 - Order Total: {}, Request Amount: {}", order.getTotal(), requestDto.getAmount());
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "결제 요청 금액이 실제 주문 금액과 일치하지 않습니다.");
        }

        // 3. 결제 내역(Payment) 생성
        Payment payment = Payment.builder()
                .orders(order)
                .paymentMethod(requestDto.getPaymentMethod())
                .amount(requestDto.getAmount())
                .status("COMPLETED") // 결제 완료
                .approvalNo(requestDto.getApprovalNo())
                .build();
        
        Payment savedPayment = paymentRepository.save(payment);

        // 4. 주문(Orders) 상태 업데이트 -> 상품 준비 중
        order.updateStatus("PREPARING_PRODUCT");

        log.info("결제 승인 및 처리 완료 - Payment ID: {}, Order ID: {}", savedPayment.getId(), order.getId());
        return PaymentResponseDto.from(savedPayment);
    }
}