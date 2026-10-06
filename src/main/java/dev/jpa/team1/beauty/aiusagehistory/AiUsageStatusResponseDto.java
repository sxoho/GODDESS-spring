package dev.jpa.team1.beauty.aiusagehistory;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiUsageStatusResponseDto {


    /**
     * 회원 번호
     */
    private Long memberId;


    /**
     * Premium 여부
     */
    private boolean premium;


    /**
     * Premium 무제한 여부
     */
    private boolean unlimited;


    /**
     * 일반회원 월 무료 한도
     */
    private Integer monthlyLimit;


    /**
     * 이번 달 무료 사용 횟수
     */
    private Long usedCount;


    /**
     * 이번 달 남은 무료 횟수
     *
     * Premium은 null
     */
    private Integer remainingCount;
}