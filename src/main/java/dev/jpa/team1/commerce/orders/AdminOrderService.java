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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final MemberRepository memberRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    public List<AdminOrderResponseDto> findAll(String loginId) {
        validateAdmin(loginId);

        return ordersRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
                .stream()
                .map(order -> AdminOrderResponseDto.from(
                        order,
                        deliveryRepository.findByOrdersId(order.getId()).orElse(null)
                ))
                .toList();
    }

    /**
     * PAID -> PREPARING -> SHIPPED -> SHIPPING -> DELIVERED
     */
    @Transactional
    public void updateStatus(
            String loginId,
            Long orderId,
            AdminOrderStatusRequestDto dto
    ) {
        validateAdmin(loginId);

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        Delivery delivery = deliveryRepository.findByOrdersId(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

        String requested = dto.getStatus().trim().toUpperCase();
        String current = order.getStatus();

        String expected = switch (current) {
            case "PAID" -> "PREPARING";
            case "PREPARING" -> "SHIPPED";
            case "SHIPPED" -> "SHIPPING";
            case "SHIPPING" -> "DELIVERED";
            default -> null;
        };

        if (expected == null || !expected.equals(requested)) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "주문 상태는 결제완료 → 상품준비중 → 배송시작 → 배송중 → 배송완료 순서로만 변경할 수 있습니다."
            );
        }

        switch (requested) {
            case "PREPARING" -> {
                order.updateStatus("PREPARING");
                delivery.markPreparing();
            }
            case "SHIPPED" -> {
                if (dto.getCompany() == null || dto.getCompany().isBlank()) {
                    throw new CustomException(
                            ErrorCode.INVALID_INPUT_VALUE,
                            "배송 시작 시 택배사를 선택해주세요."
                    );
                }

                String trackingNo = generateTrackingNo();

                delivery.markShipped(
                        dto.getCompany().trim(),
                        trackingNo
                );
                order.updateStatus("SHIPPED");
            }
            case "SHIPPING" -> {
                delivery.markShipping();
                order.updateStatus("SHIPPING");
            }
            case "DELIVERED" -> {
                delivery.markDelivered();
                order.updateStatus("DELIVERED");
            }
            default -> throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private String generateTrackingNo() {
        String random = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 14)
                .toUpperCase();

        return "GD" + random;
    }

    @Transactional
    public void approveCancelRequest(String loginId, Long orderId) {
        validateAdmin(loginId);

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!"CANCEL_REQUESTED".equals(order.getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "취소 요청 상태의 주문만 승인할 수 있습니다."
            );
        }

        restoreStock(orderId);

        Payment payment = paymentRepository.findByOrdersId(orderId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "결제 정보를 찾을 수 없습니다."
                ));

        if ("PAID".equals(payment.getStatus())) {
            payment.cancel();
        }

        deliveryRepository.findByOrdersId(orderId).ifPresent(Delivery::cancel);
        order.cancel();
    }

    @Transactional
    public void rejectCancelRequest(String loginId, Long orderId) {
        validateAdmin(loginId);

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!"CANCEL_REQUESTED".equals(order.getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "취소 요청 상태의 주문만 거절할 수 있습니다."
            );
        }

        order.updateStatus("PREPARING");
    }

    private void restoreStock(Long orderId) {
        List<OrderDetail> details =
                orderDetailRepository.findByOrdersIdOrderByIdAsc(orderId);

        for (OrderDetail detail : details) {
            detail.getProductOption().increaseStock(detail.getQuantity());
        }
    }

    private Member validateAdmin(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getGrade() == null
                || member.getGrade().getId() == null
                || member.getGrade().getId() != 4L) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "관리자만 접근할 수 있습니다."
            );
        }

        return member;
    }
}
