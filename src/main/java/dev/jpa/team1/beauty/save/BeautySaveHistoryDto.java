package dev.jpa.team1.beauty.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class BeautySaveHistoryDto {


    private Long beautySaveId;

    private Long beautyResultId;

    private Long beautyTryonId;


    private String saveName;

    private String memo;

    private LocalDateTime createdAt;


    private String beautyType;

    private String tryonStatus;


    /**
     * AI 적용 결과
     */
    private String resultFilePath;


    /**
     * AI 적용 전 회원 사진
     */
    private String beforeFilePath;


    /**
     * 적용한 상품 / 헤어 정보
     */
    private List<ItemDto> items;


    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemDto {

        private String itemType;

        private String styleValue;


        private Long productId;

        private String productName;

        private String brand;


        private Long productColorId;

        private String colorName;

        private String colorCode;


        private Integer price;
    }
}