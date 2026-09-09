package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.cart.Cart;
import dev.jpa.team1.commerce.cart.CartRepository;
import dev.jpa.team1.commerce.delivery.Delivery;
import dev.jpa.team1.commerce.delivery.DeliveryRepository;
import dev.jpa.team1.commerce.delivery.DeliveryResponseDto;
import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
import dev.jpa.team1.commerce.orderdetail.OrderDetailResponseDto;
import dev.jpa.team1.commerce.payment.Payment;
import dev.jpa.team1.commerce.payment.PaymentRepository;
import dev.jpa.team1.commerce.payment.PaymentResponseDto;
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
public class OrderService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderResponseDto createOrder(
            String loginId,
            OrderCreateRequestDto dto
    ) {

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.MEMBER_NOT_FOUND)
                );

        List<Cart> cartItems =
                cartRepository.findAllById(dto.getCartIds());

        if (cartItems.isEmpty()) {
            throw new CustomException(
                    ErrorCode.CART_ITEM_NOT_FOUND
            );
        }

        for (Cart cart : cartItems) {

            if (!cart.getMember().getId().equals(member.getId())) {
                throw new CustomException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "다른 회원의 장바구니 항목이 포함되어 있습니다."
                );
            }
        }

        String orderName =
                cartItems.getFirst()
                        .getProductOption()
                        .getProduct()
                        .getName();

        if (cartItems.size() > 1) {
            orderName += " 외 " + (cartItems.size() - 1) + "건";
        }

        int productTotal = 0;
        int discount = 0;

        for (Cart cart : cartItems) {

            var option = cart.getProductOption();
            var product = option.getProduct();

            if (!"ON_SALE".equals(product.getStatus())
                    || !"Y".equals(option.getVisible())) {

                throw new CustomException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "현재 구매할 수 없는 상품이 포함되어 있습니다."
                );
            }

            if (option.getStock() < cart.getQuantity()) {
                throw new CustomException(
                        ErrorCode.OUT_OF_STOCK
                );
            }

            int unitOriginalPrice =
                    product.getPrice()
                            + (option.getAddPrice() == null
                            ? 0
                            : option.getAddPrice());

            int discountRate =
                    product.getDiscount() == null
                            ? 0
                            : product.getDiscount();

            int unitSalePrice = Math.round(
                    unitOriginalPrice
                            * (1 - discountRate / 100.0f)
            );

            productTotal +=
                    unitOriginalPrice * cart.getQuantity();

            discount +=
                    (unitOriginalPrice - unitSalePrice)
                            * cart.getQuantity();
        }

        int discountedProductTotal =
                productTotal - discount;

        int deliveryFee =
                discountedProductTotal >= 50000
                        ? 0
                        : 3000;

        int total =
                discountedProductTotal + deliveryFee;

        Orders order =
                ordersRepository.save(
                        Orders.builder()
                                .member(member)
                                .orderName(orderName)
                                .productTotal(productTotal)
                                .deliveryFee(deliveryFee)
                                .discount(discount)
                                .total(total)
                                .status("ORDERED")
                                .build()
                );

        for (Cart cart : cartItems) {

            var option = cart.getProductOption();
            var product = option.getProduct();

            int unitOriginalPrice =
                    product.getPrice()
                            + (option.getAddPrice() == null
                            ? 0
                            : option.getAddPrice());

            int discountRate =
                    product.getDiscount() == null
                            ? 0
                            : product.getDiscount();

            int unitSalePrice = Math.round(
                    unitOriginalPrice
                            * (1 - discountRate / 100.0f)
            );

            option.decreaseStock(cart.getQuantity());

            orderDetailRepository.save(
                    OrderDetail.builder()
                            .orders(order)
                            .productOption(option)
                            .productName(product.getName())
                            .colorName(
                                    option.getProductColor() != null
                                            ? option.getProductColor()
                                            .getColorMaster()
                                            .getColorName()
                                            : null
                            )
                            .variantName(
                                    option.getProductVariant() != null
                                            ? option.getProductVariant()
                                            .getVariantName()
                                            : null
                            )
                            .price(unitSalePrice)
                            .quantity(cart.getQuantity())
                            .build()
            );
        }

        deliveryRepository.save(
                Delivery.builder()
                        .orders(order)
                        .recipient(dto.getRecipient())
                        .tel(dto.getTel())
                        .zipcode(dto.getZipcode())
                        .address1(dto.getAddress1())
                        .address2(dto.getAddress2())
                        .request(dto.getRequest())
                        .status("PREPARING")
                        .build()
        );

        cartRepository.deleteAll(cartItems);

        return OrderResponseDto.from(order);
    }

    /**
     * 내 주문내역 목록
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders(String loginId) {

        Member member =
                memberRepository.findByLoginId(loginId)
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                        );

        return ordersRepository
                .findByMemberIdOrderByCreatedAtDesc(member.getId())
                .stream()
                .map(OrderResponseDto::from)
                .toList();
    }

    /**
     * 내 주문 상세
     *
     * 주문 기본정보뿐 아니라
     * ORDER_DETAIL / PAYMENT / DELIVERY까지 함께 조회한다.
     */
    @Transactional(readOnly = true)
    public OrderHistoryDetailResponseDto getMyOrder(
            String loginId,
            Long orderId
    ) {

        Member member =
                memberRepository.findByLoginId(loginId)
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                        );

        Orders order =
                ordersRepository.findById(orderId)
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.ORDER_NOT_FOUND
                                )
                        );

        if (!order.getMember().getId().equals(member.getId())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "해당 주문을 조회할 권한이 없습니다."
            );
        }

        List<OrderDetailResponseDto> orderDetails =
                orderDetailRepository
                        .findByOrdersIdOrderByIdAsc(orderId)
                        .stream()
                        .map(OrderDetailResponseDto::from)
                        .toList();

        PaymentResponseDto payment =
                paymentRepository.findByOrdersId(orderId)
                        .map(PaymentResponseDto::from)
                        .orElse(null);

        DeliveryResponseDto delivery =
                deliveryRepository.findByOrdersId(orderId)
                        .map(DeliveryResponseDto::from)
                        .orElse(null);

        return OrderHistoryDetailResponseDto.builder()
                .id(order.getId())
                .memberId(order.getMember().getId())
                .orderName(order.getOrderName())
                .productTotal(order.getProductTotal())
                .deliveryFee(order.getDeliveryFee())
                .discount(order.getDiscount())
                .total(order.getTotal())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .cancelDate(order.getCancelDate())
                .orderDetails(orderDetails)
                .payment(payment)
                .delivery(delivery)
                .build();
    }
}
