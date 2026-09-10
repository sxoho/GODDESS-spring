package dev.jpa.team1.commerce.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {

    /**
     * 장바구니 주문 시 사용한다.
     * BUY NOW 주문에서는 null 또는 빈 배열이어도 된다.
     */
    private List<Long> cartIds;

    /**
     * 바로구매(BUY NOW) 시 사용한다.
     * 장바구니를 거치지 않고 상품 옵션과 수량을 직접 주문한다.
     */
    private List<@Valid BuyNowItemRequestDto> buyNowItems;

    // 배송지(DELIVERY) 정보 맵핑
    @NotBlank(message = "수령인을 입력해주세요.")
    private String recipient;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String tel;

    @NotBlank(message = "우편번호를 입력해주세요.")
    private String zipcode;

    @NotBlank(message = "기본 주소를 입력해주세요.")
    private String address1;

    @NotBlank(message = "상세 주소를 입력해주세요.")
    private String address2;

    private String request;
}
