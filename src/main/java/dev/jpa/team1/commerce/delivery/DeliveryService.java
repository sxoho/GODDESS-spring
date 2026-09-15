package dev.jpa.team1.commerce.delivery;

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
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;

    // 내 주문의 배송 정보 조회 (사용자용)
    @Transactional(readOnly = true)
    public DeliveryResponseDto getMyDeliveryInfo(String loginId, Long orderId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 본인의 주문인지 권한 체크
        if (!order.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "해당 배송 정보를 조회할 권한이 없습니다.");
        }

        Delivery delivery = deliveryRepository.findByOrdersId(order.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

        return DeliveryResponseDto.from(delivery);
    }

    // 운송장 번호 등록 및 배송 상태 업데이트 (관리자용)
    @Transactional
    public DeliveryResponseDto updateTrackingInfo(Long deliveryId, DeliveryTrackingUpdateRequestDto requestDto) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

        Orders order = delivery.getOrders();

        if (!"PREPARING".equals(order.getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "상품준비중 주문만 배송 시작 처리할 수 있습니다."
            );
        }

        // ERD의 company / tracking_no / shipping_date를 사용해 배송 시작 처리
        delivery.markShipped(requestDto.getCompany(), requestDto.getTrackingNo());
        order.updateStatus("SHIPPED");

        log.info("운송장 번호 등록 완료 - Delivery ID: {}, Tracking No: {}", deliveryId, requestDto.getTrackingNo());
        
        return DeliveryResponseDto.from(delivery);
    }
}