package dev.jpa.team1.commerce.cart;
import dev.jpa.team1.exception.*;
import dev.jpa.team1.member.member.*;
import dev.jpa.team1.product.productoption.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service @RequiredArgsConstructor
public class CartService {
 private final CartRepository cartRepository; private final MemberRepository memberRepository; private final ProductOptionRepository optionRepository;
 @Transactional public CartResponseDto addCartItem(String loginId,CartRequestDto d){Member m=memberRepository.findByLoginId(loginId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND)); ProductOption o=optionRepository.findById(d.getProductOptionId()).orElseThrow(()->new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND)); Optional<Cart> ex=cartRepository.findByMemberIdAndProductOptionId(m.getId(),o.getId()); if(ex.isPresent()){Cart c=ex.get();c.changeQuantity(c.getQuantity()+Math.max(1,d.getQuantity()));return CartResponseDto.from(c);} return CartResponseDto.from(cartRepository.save(Cart.builder().member(m).productOption(o).quantity(Math.max(1,d.getQuantity())).build()));}
 @Transactional(readOnly=true) public List<CartResponseDto> getMyCart(String loginId){Member m=memberRepository.findByLoginId(loginId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));return cartRepository.findByMemberIdOrderByCreatedAtDesc(m.getId()).stream().map(CartResponseDto::from).toList();}
 @Transactional public void deleteCartItem(String loginId,Long cartId){Member m=memberRepository.findByLoginId(loginId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));Cart c=cartRepository.findById(cartId).orElseThrow(()->new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));if(!c.getMember().getId().equals(m.getId()))throw new CustomException(ErrorCode.INVALID_INPUT_VALUE,"해당 장바구니 항목을 삭제할 권한이 없습니다.");cartRepository.delete(c);}
}
