package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.cart.Cart;
import dev.jpa.team1.commerce.cart.CartRepository;
import dev.jpa.team1.commerce.delivery.Delivery;
import dev.jpa.team1.commerce.delivery.DeliveryRepository;
import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
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

    @Transactional
    public OrderResponseDto createOrder(String loginId, OrderCreateRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Cart> cartItems = cartRepository.findAllById(requestDto.getCartIds());
        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        // 1. 주문 총액 및 대표 주문명(orderName) 산출
        int productTotal = 0;
        String orderName = cartItems.getFirst().getProductOption().getProduct().getName();
        if (cartItems.size() > 1) {
            orderName += " 외 " + (cartItems.size() - 1) + "건";
        }

        for (Cart cart : cartItems) {
            int itemPrice = cart.getProductOption().getProduct().getPrice() + cart.getProductOption().getAddPrice();
            productTotal += (itemPrice * cart.getQuantity());
        }

        int deliveryFee = productTotal >= 50000 ? 0 : 3000;
        int discount = 0;
        int total = productTotal + deliveryFee - discount;

        // 2. ORDERS 테이블 데이터 적재
        Orders newOrder = Orders.builder()
                .member(member)
                .orderName(orderName)
                .productTotal(productTotal)
                .deliveryFee(deliveryFee)
                .discount(discount)
                .total(total)
                .status("PENDING_PAYMENT")
                .build();
        
        Orders savedOrder = ordersRepository.save(newOrder);

        // 3. ORDER_DETAIL 테이블 데이터 적재 (스냅샷 보존)
        for (Cart cart : cartItems) {
            if (cart.getProductOption().getStock() < cart.getQuantity()) {
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }
            // 주문 시 재고 차감 비즈니스 로직
            cart.getProductOption().decreaseStock(cart.getQuantity());

            int snapshotPrice = cart.getProductOption().getProduct().getPrice() + cart.getProductOption().getAddPrice();
            
            // 색상 마스터 테이블(PRODUCT_COLOR_INFO) 참조 추출 로직 반영
            String extractedColorName = cart.getProductOption().getProductColor().getProductColorInfo().getColorName();
            String extractedSizeName = cart.getProductOption().getProductSize().getSizeName();

            OrderDetail detail = OrderDetail.builder()
                    .orders(savedOrder)
                    .productOption(cart.getProductOption())
                    .quickFittingResult(cart.getQuickFittingResult())
                    .coordFittingResult(cart.getCoordFittingResult())
                    .productName(cart.getProductOption().getProduct().getName())
                    .colorName(extractedColorName) 
                    .sizeName(extractedSizeName)
                    .price(snapshotPrice)
                    .quantity(cart.getQuantity())
                    .build();

            orderDetailRepository.save(detail);
        }

        // 4. DELIVERY 테이블 데이터 적재 (주문 테이블과 1:1 연관)
        Delivery delivery = Delivery.builder()
                .orders(savedOrder)
                .recipient(requestDto.getRecipient())
                .tel(requestDto.getTel())
                .zipcode(requestDto.getZipcode())
                .address1(requestDto.getAddress1())
                .address2(requestDto.getAddress2())
                .request(requestDto.getRequest())
                .status("PREPARING") 
                .build();
        
        deliveryRepository.save(delivery);

        // 5. 주문 처리된 장바구니 아이템 삭제
        cartRepository.deleteAll(cartItems);

        return OrderResponseDto.from(savedOrder);
    }
}