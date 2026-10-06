package dev.jpa.team1.member.subscription;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SubscriptionRequestDto {


    /**
     * 회원 번호
     */
    private Long memberId;


    /**
     * MONTHLY
     * YEARLY
     */
    private String planType;
}