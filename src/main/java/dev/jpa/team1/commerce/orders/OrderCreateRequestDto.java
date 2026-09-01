package dev.jpa.team1.commerce.orders;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {

    @NotEmpty(message = "주문할 장바구니 아이템을 선택해주세요.")
    private List<Long> cartIds;

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

    private String request; // 배송 요청사항
}