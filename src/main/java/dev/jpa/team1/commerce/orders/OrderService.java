package dev.jpa.team1.commerce.orders;

import dev.jpa.team1.commerce.cart.*;
import dev.jpa.team1.commerce.delivery.*;
import dev.jpa.team1.commerce.orderdetail.*;
import dev.jpa.team1.exception.*;
import dev.jpa.team1.member.member.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public OrderResponseDto createOrder(String loginId, OrderCreateRequestDto dto) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<Cart> cartItems = cartRepository.findAllById(dto.getCartIds());
        if (cartItems.isEmpty()) throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);
        for (Cart c:cartItems) if(!c.getMember().getId().equals(member.getId())) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE,"다른 회원의 장바구니 항목이 포함되어 있습니다.");

        int productTotal=0;
        String orderName=cartItems.getFirst().getProductOption().getProduct().getName();
        if(cartItems.size()>1) orderName += " 외 " + (cartItems.size()-1) + "건";
        for(Cart c:cartItems){ int itemPrice=c.getProductOption().getProduct().getPrice()+c.getProductOption().getAddPrice(); productTotal += itemPrice*c.getQuantity(); }
        int deliveryFee=productTotal>=50000?0:3000;
        int discount=0;
        int total=productTotal+deliveryFee-discount;
        Orders order=ordersRepository.save(Orders.builder().member(member).orderName(orderName).productTotal(productTotal).deliveryFee(deliveryFee).discount(discount).total(total).status("ORDERED").build());

        for(Cart c:cartItems){
            var o=c.getProductOption();
            if(o.getStock()<c.getQuantity()) throw new CustomException(ErrorCode.OUT_OF_STOCK);
            o.decreaseStock(c.getQuantity());
            int snapshot=o.getProduct().getPrice()+o.getAddPrice();
            orderDetailRepository.save(OrderDetail.builder().orders(order).productOption(o).productName(o.getProduct().getName())
                    .colorName(o.getProductColor()!=null?o.getProductColor().getColorMaster().getColorName():null)
                    .variantName(o.getProductVariant()!=null?o.getProductVariant().getVariantName():null)
                    .price(snapshot).quantity(c.getQuantity()).build());
        }

        deliveryRepository.save(Delivery.builder().orders(order).recipient(dto.getRecipient()).tel(dto.getTel()).zipcode(dto.getZipcode()).address1(dto.getAddress1()).address2(dto.getAddress2()).request(dto.getRequest()).status("PREPARING").build());
        cartRepository.deleteAll(cartItems);
        return OrderResponseDto.from(order);
    }
}
