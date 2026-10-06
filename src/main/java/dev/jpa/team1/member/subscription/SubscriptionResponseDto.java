package dev.jpa.team1.member.subscription;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDto {


    private Long subscriptionId;

    private Long memberId;

    private String planType;

    private String status;

    private Long price;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static SubscriptionResponseDto from(
            Subscription subscription
    ) {

        return new SubscriptionResponseDto(
                subscription.getId(),
                subscription
                        .getMember()
                        .getId(),
                subscription.getPlanType(),
                subscription.getStatus(),
                subscription.getPrice(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getCreatedAt(),
                subscription.getUpdatedAt()
        );
    }
}