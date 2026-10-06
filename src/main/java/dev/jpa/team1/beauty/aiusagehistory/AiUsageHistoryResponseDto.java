package dev.jpa.team1.beauty.aiusagehistory;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiUsageHistoryResponseDto {


    private Long id;

    private Long memberId;

    private String usageType;

    private LocalDateTime usedAt;


    public static AiUsageHistoryResponseDto from(
            AiUsageHistory history
    ) {

        return new AiUsageHistoryResponseDto(
                history.getId(),
                history.getMember().getId(),
                history.getUsageType(),
                history.getUsedAt()
        );
    }
}