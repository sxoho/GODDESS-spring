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
public class SubscriptionStatusResponseDto {


    private Long memberId;

    private boolean premium;

    private String planType;

    private String status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}