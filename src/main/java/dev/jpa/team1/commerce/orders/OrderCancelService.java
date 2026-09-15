package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.delivery.Delivery;
import dev.jpa.team1.commerce.delivery.DeliveryRepository;
import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
import dev.jpa.team1.commerce.payment.Payment;
import dev.jpa.team1.commerce.payment.PaymentRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCancelService {

    private final MemberRepository memberRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * ERD 기준 고객 취소 흐름
     * PAYMENT_WAITING -> 즉시 CANCELLED
     * PAID            -> 즉시 CANCELLED + 결제취소 + 재고복구
     * PREPARING       -> CANCEL_REQUESTED
     * SHIPPED 이후    -> 주문취소 불가
     */
    @Transactional
    public String cancelOrRequest(String loginId, Long orderId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "본인의 주문만 취소할 수 있습니다."
            );
        }

        return switch (order.getStatus()) {
            case "PAYMENT_WAITING" -> {
                cancelUnpaid(order);
                yield "CANCELLED";
            }
            case "PAID" -> {
                cancelPaid(order);
                yield "CANCELLED";
            }
            case "PREPARING" -> {
                order.requestCancel();
                yield "CANCEL_REQUESTED";
            }
            case "CANCEL_REQUESTED" -> "CANCEL_REQUESTED";
            default -> throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "배송이 시작된 주문은 취소할 수 없습니다."
            );
        };
    }

    private void cancelUnpaid(Orders order) {
        deliveryRepository.findByOrdersId(order.getId())
                .ifPresent(Delivery::cancel);
        order.cancel();
    }

    private void cancelPaid(Orders order) {
        restoreStock(order.getId());

        Payment payment = paymentRepository.findByOrdersId(order.getId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "결제 완료 정보를 찾을 수 없습니다."
                ));

        if (!"PAID".equals(payment.getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "취소 가능한 결제 상태가 아닙니다."
            );
        }

        payment.cancel();
        deliveryRepository.findByOrdersId(order.getId())
                .ifPresent(Delivery::cancel);
        order.cancel();
    }

    private void restoreStock(Long orderId) {
        List<OrderDetail> details =
                orderDetailRepository.findByOrdersIdOrderByIdAsc(orderId);

        for (OrderDetail detail : details) {
            detail.getProductOption().increaseStock(detail.getQuantity());
        }
    }
}
