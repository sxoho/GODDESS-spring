package dev.jpa.team1.commerce.cart;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.productcolorimage.ProductColorImageRepository;
import dev.jpa.team1.product.productoption.ProductOption;
import dev.jpa.team1.product.productoption.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductColorImageRepository imageRepository;

    @Transactional
    public CartResponseDto addCartItem(String loginId, CartRequestDto requestDto) {
        Member member = getMember(loginId);

        ProductOption option = optionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));

        validatePurchasableOption(option);

        int requestQuantity = Math.max(1, requestDto.getQuantity());

        Optional<Cart> existing = cartRepository.findByMemberIdAndProductOptionId(
                member.getId(),
                option.getId()
        );

        if (existing.isPresent()) {
            Cart cart = existing.get();
            int nextQuantity = cart.getQuantity() + requestQuantity;

            validateStock(option, nextQuantity);
            cart.changeQuantity(nextQuantity);

            return toResponse(cart);
        }

        validateStock(option, requestQuantity);

        Cart cart = cartRepository.save(
                Cart.builder()
                        .member(member)
                        .productOption(option)
                        .quantity(requestQuantity)
                        .build()
        );

        return toResponse(cart);
    }

    @Transactional(readOnly = true)
    public List<CartResponseDto> getMyCart(String loginId) {
        Member member = getMember(loginId);

        return cartRepository.findByMemberIdOrderByCreatedAtDesc(member.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CartResponseDto updateQuantity(
            String loginId,
            Long cartId,
            CartQuantityRequestDto requestDto
    ) {
        Member member = getMember(loginId);
        Cart cart = getOwnedCart(member, cartId);
        ProductOption option = cart.getProductOption();

        validatePurchasableOption(option);
        validateStock(option, requestDto.getQuantity());

        cart.changeQuantity(requestDto.getQuantity());
        return toResponse(cart);
    }

    @Transactional
    public void deleteCartItem(String loginId, Long cartId) {
        Member member = getMember(loginId);
        Cart cart = getOwnedCart(member, cartId);
        cartRepository.delete(cart);
    }

    private Member getMember(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Cart getOwnedCart(Member member, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!cart.getMember().getId().equals(member.getId())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "해당 장바구니 항목에 접근할 권한이 없습니다."
            );
        }

        return cart;
    }

    private void validatePurchasableOption(ProductOption option) {
        if (!"Y".equals(option.getVisible())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "현재 판매하지 않는 상품 옵션입니다."
            );
        }

        if (!"ON_SALE".equals(option.getProduct().getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "현재 구매할 수 없는 상품입니다."
            );
        }
    }

    private void validateStock(ProductOption option, int quantity) {
        if (quantity < 1 || option.getStock() < quantity) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }
    }

    private CartResponseDto toResponse(Cart cart) {
        ProductOption option = cart.getProductOption();
        String filePath = null;

        if (option.getProductColor() != null) {
            Long productColorId = option.getProductColor().getId();

            filePath = imageRepository
                    .findFirstByProductColorIdAndIsMainOrderBySortOrderAsc(
                            productColorId,
                            "Y"
                    )
                    .map(image -> image.getFilePath())
                    .orElseGet(() -> imageRepository
                            .findByProductColorIdAndImageTypeOrderBySortOrderAsc(
                                    productColorId,
                                    "THUMBNAIL"
                            )
                            .stream()
                            .findFirst()
                            .map(image -> image.getFilePath())
                            .orElse(null));
        }

        return CartResponseDto.from(cart, filePath);
    }
}
