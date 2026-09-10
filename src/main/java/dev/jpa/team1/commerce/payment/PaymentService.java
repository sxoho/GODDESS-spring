package dev.jpa.team1.commerce.payment;

import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.commerce.orders.OrdersRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.productoption.ProductOption;
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
    public PaymentResponseDto processPayment(
            String loginId,
            PaymentApprovalRequestDto requestDto
    ) {
        Member member =
                memberRepository.findByLoginId(loginId)
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                        );

        Orders order =
                ordersRepository.findById(requestDto.getOrderId())
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.ORDER_NOT_FOUND
                                )
                        );

        if (!order.getMember().getId().equals(member.getId())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "본인의 주문만 결제할 수 있습니다."
            );
        }

        /*
         * GODDESS DB CK_ORDERS_STATUS 기준:
         * 결제 전 주문은 PAYMENT_WAITING.
         */
        if (!"PAYMENT_WAITING".equals(order.getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "결제 가능한 주문 상태가 아닙니다."
            );
        }

        if (paymentRepository.existsByOrdersId(order.getId())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "이미 결제 처리된 주문입니다."
            );
        }

        /*
         * 클라이언트 금액 변조 방지:
         * 실제 ORDERS.TOTAL 값과 반드시 같아야 한다.
         */
        if (!order.getTotal().equals(requestDto.getAmount())) {
            log.error(
                    "결제 금액 불일치 - Order Total: {}, Request Amount: {}",
                    order.getTotal(),
                    requestDto.getAmount()
            );

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "결제 요청 금액이 실제 주문 금액과 일치하지 않습니다."
            );
        }

        /*
         * 주문 생성(PAYMENT_WAITING) 단계에서는 재고를 차감하지 않는다.
         * 결제 승인 직전에 주문 상세의 옵션/수량을 다시 확인하면서 차감한다.
         * 재고 부족 시 decreaseStock()이 예외를 발생시키고,
         * @Transactional에 의해 결제 저장/상태 변경도 함께 롤백된다.
         */
        for (OrderDetail detail : order.getOrderDetails()) {
            ProductOption option = detail.getProductOption();
            option.decreaseStock(detail.getQuantity());
        }

        /*
         * GODDESS DB CK_PAYMENT_STATUS 허용값:
         *
         * READY / PAID / CANCELLED / FAILED
         *
         * 결제 승인 성공 시 PAID.
         */
        Payment payment =
                Payment.builder()
                        .orders(order)
                        .paymentMethod(requestDto.getPaymentMethod())
                        .amount(requestDto.getAmount())
                        .status("PAID")
                        .approvalNo(requestDto.getApprovalNo())
                        .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        /*
         * 결제 완료 직후 주문 상태는 PAID.
         * PREPARING은 이후 상품 준비 단계에서 변경.
         */
        order.updateStatus("PAID");

        log.info(
                "결제 승인 완료 - Payment ID: {}, Order ID: {}",
                savedPayment.getId(),
                order.getId()
        );

        return PaymentResponseDto.from(savedPayment);
    }
}
