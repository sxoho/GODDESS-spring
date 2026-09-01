package dev.jpa.team1.commerce.cart;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResult;
import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResultRepository;
import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResult;
import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResultRepository;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.productcolorimage.ProductColorImage;
import dev.jpa.team1.product.productcolorimage.ProductColorImageRepository;
import dev.jpa.team1.product.productoption.ProductOption;
import dev.jpa.team1.product.productoption.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductOptionRepository productOptionRepository;
    private final QuickFittingResultRepository quickFittingResultRepository;
    private final CoordFittingResultRepository coordFittingResultRepository;
    private final ProductColorImageRepository productColorImageRepository;

    // 장바구니 상품 추가
    @Transactional
    public CartResponseDto addCartItem(String loginId, CartRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ProductOption option = productOptionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));

        QuickFittingResult quickResult = null;
        if (requestDto.getQuickFittingResultId() != null) {
            quickResult = quickFittingResultRepository.findById(requestDto.getQuickFittingResultId()).orElse(null);
        }

        CoordFittingResult coordResult = null;
        if (requestDto.getCoordFittingResultId() != null) {
            coordResult = coordFittingResultRepository.findById(requestDto.getCoordFittingResultId()).orElse(null);
        }

        String filePath = requestDto.getFilePath();
        if (filePath == null || filePath.isBlank()) {
            Long productColorId = option.getProductColor().getId();
            filePath = productColorImageRepository.findFirstByProductColorIdAndIsMainOrderBySortOrderAsc(productColorId, "Y")
                    .map(ProductColorImage::getFilePath)
                    .orElseGet(() -> productColorImageRepository.findByProductColorIdOrderBySortOrderAsc(productColorId)
                            .stream().findFirst().map(ProductColorImage::getFilePath).orElse(null));
        }

        Cart cart = Cart.builder()
                .member(member)
                .productOption(option)
                .quickFittingResult(quickResult)
                .coordFittingResult(coordResult)
                .filePath(filePath)
                .quantity(requestDto.getQuantity())
                .build();

        Cart savedCart = cartRepository.save(cart);
        return CartResponseDto.from(savedCart);
    }

    // 장바구니 불러오기
    public List<CartResponseDto> getMyCart(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Cart> cartList = cartRepository.findByMemberIdOrderByCreatedAtDesc(member.getId());

        List<CartResponseDto> cartResponseDtoList = new ArrayList<>();
        for (Cart cart : cartList) {
            cartResponseDtoList.add(CartResponseDto.from(cart));
        }

        return cartResponseDtoList;
    }

    // 장바구니 항목 삭제
    @Transactional
    public void deleteCartItem(String loginId, Long cartId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

        // 본인의 장바구니 항목인지 검증
        if (!cart.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "해당 장바구니 항목을 삭제할 권한이 없습니다.");
        }

        cartRepository.delete(cart);
    }
}
