package dev.jpa.team1.beauty.tryon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BeautyApplyRequestDto {

    /**
     * 로그인 회원 ID
     */
    private Long memberId;


    /**
     * 메이크업
     */
    private ProductSelection base;

    private ProductSelection lip;

    private ProductSelection cheek;

    private ProductSelection eye;


    /**
     * 헤어
     */
    private String hairStyle;

    private String hairColor;


    /**
     * 실제 상품 / 색상 선택 정보
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductSelection {

        private Long productId;

        private String productName;

        private String brand;

        private Long colorId;

        private String colorName;

        private String colorCode;
    }
}