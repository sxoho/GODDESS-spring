package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.cart.Cart;
import dev.jpa.team1.commerce.cart.CartRepository;
import dev.jpa.team1.commerce.delivery.Delivery;
import dev.jpa.team1.commerce.delivery.DeliveryRepository;
import dev.jpa.team1.commerce.delivery.DeliveryResponseDto;
import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
import dev.jpa.team1.commerce.orderdetail.OrderDetailResponseDto;
import dev.jpa.team1.commerce.payment.PaymentRepository;
import dev.jpa.team1.commerce.payment.PaymentResponseDto;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.productoption.ProductOption;
import dev.jpa.team1.product.productoption.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ProductOptionRepository productOptionRepository;

    /**
     * 주문 생성
     *
     * 1) 장바구니 주문: cartIds 사용
     * 2) 바로구매: buyNowItems 사용
     *
     * 두 방식 중 정확히 하나만 전달되어야 한다.
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

        boolean hasCartIds =
                dto.getCartIds() != null
                        && !dto.getCartIds().isEmpty();

        boolean hasBuyNowItems =
                dto.getBuyNowItems() != null
                        && !dto.getBuyNowItems().isEmpty();

        if (hasCartIds == hasBuyNowItems) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "장바구니 주문 또는 바로구매 중 하나의 방식만 선택해주세요."
            );
        }

        List<Cart> cartItems = new ArrayList<>();
        List<PurchaseItem> purchaseItems = new ArrayList<>();

        if (hasCartIds) {
            prepareCartOrder(
                    member,
                    dto.getCartIds(),
                    cartItems,
                    purchaseItems
            );
        } else {
            prepareBuyNowOrder(
                    dto.getBuyNowItems(),
                    purchaseItems
            );
        }

        if (purchaseItems.isEmpty()) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "주문할 상품이 없습니다."
            );
        }

        String orderName = createOrderName(purchaseItems);

        int productTotal = 0;
        int discount = 0;

        for (PurchaseItem item : purchaseItems) {
            ProductOption option = item.option();
            int quantity = item.quantity();

            validatePurchasable(option, quantity);

            int unitOriginalPrice = getUnitOriginalPrice(option);
            int unitSalePrice = getUnitSalePrice(option, unitOriginalPrice);

            productTotal += unitOriginalPrice * quantity;
            discount +=
                    (unitOriginalPrice - unitSalePrice)
                            * quantity;
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

        for (PurchaseItem item : purchaseItems) {
            ProductOption option = item.option();
            int quantity = item.quantity();
            var product = option.getProduct();

            int unitOriginalPrice = getUnitOriginalPrice(option);
            int unitSalePrice = getUnitSalePrice(option, unitOriginalPrice);

            option.decreaseStock(quantity);

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
                            .quantity(quantity)
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

        /*
         * 장바구니 주문에서만 선택한 장바구니 상품을 삭제한다.
         * BUY NOW는 기존 장바구니를 전혀 건드리지 않는다.
         */
        if (hasCartIds) {
            cartRepository.deleteAll(cartItems);
        }

        return OrderResponseDto.from(order);
    }

    /**
     * 장바구니 주문 준비
     */
    private void prepareCartOrder(
            Member member,
            List<Long> cartIds,
            List<Cart> cartItems,
            List<PurchaseItem> purchaseItems
    ) {
        for (Long cartId : cartIds) {
            if (cartId == null) {
                throw new CustomException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "잘못된 장바구니 항목이 포함되어 있습니다."
                );
            }

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() ->
                            new CustomException(
                                    ErrorCode.CART_ITEM_NOT_FOUND
                            )
                    );

            if (!cart.getMember().getId().equals(member.getId())) {
                throw new CustomException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "다른 회원의 장바구니 항목이 포함되어 있습니다."
                );
            }

            cartItems.add(cart);
            purchaseItems.add(
                    new PurchaseItem(
                            cart.getProductOption(),
                            cart.getQuantity()
                    )
            );
        }
    }

    /**
     * 바로구매 주문 준비
     *
     * CART 테이블을 조회/추가/수정하지 않고 PRODUCT_OPTION을 직접 조회한다.
     */
    private void prepareBuyNowOrder(
            List<BuyNowItemRequestDto> buyNowItems,
            List<PurchaseItem> purchaseItems
    ) {
        for (BuyNowItemRequestDto item : buyNowItems) {
            if (item == null
                    || item.getProductOptionId() == null
                    || item.getQuantity() == null
                    || item.getQuantity() <= 0) {

                throw new CustomException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "바로구매 상품 옵션과 수량을 확인해주세요."
                );
            }

            ProductOption option =
                    productOptionRepository
                            .findById(item.getProductOptionId())
                            .orElseThrow(() ->
                                    new CustomException(
                                            ErrorCode.PRODUCT_OPTION_NOT_FOUND
                                    )
                            );

            purchaseItems.add(
                    new PurchaseItem(
                            option,
                            item.getQuantity()
                    )
            );
        }
    }

    private void validatePurchasable(
            ProductOption option,
            int quantity
    ) {
        var product = option.getProduct();

        if (!"ON_SALE".equals(product.getStatus())
                || !"Y".equals(option.getVisible())) {

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "현재 구매할 수 없는 상품이 포함되어 있습니다."
            );
        }

        if (option.getStock() < quantity) {
            throw new CustomException(
                    ErrorCode.OUT_OF_STOCK
            );
        }
    }

    private String createOrderName(
            List<PurchaseItem> purchaseItems
    ) {
        String orderName =
                purchaseItems.getFirst()
                        .option()
                        .getProduct()
                        .getName();

        if (purchaseItems.size() > 1) {
            orderName +=
                    " 외 "
                            + (purchaseItems.size() - 1)
                            + "건";
        }

        return orderName;
    }

    private int getUnitOriginalPrice(
            ProductOption option
    ) {
        return option.getProduct().getPrice()
                + (option.getAddPrice() == null
                ? 0
                : option.getAddPrice());
    }

    private int getUnitSalePrice(
            ProductOption option,
            int unitOriginalPrice
    ) {
        int discountRate =
                option.getProduct().getDiscount() == null
                        ? 0
                        : option.getProduct().getDiscount();

        return Math.round(
                unitOriginalPrice
                        * (1 - discountRate / 100.0f)
        );
    }

    /**
     * 장바구니/바로구매를 같은 주문 생성 로직으로 처리하기 위한 내부 모델
     */
    private record PurchaseItem(
            ProductOption option,
            int quantity
    ) {
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
