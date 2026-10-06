package dev.jpa.team1.beauty.aiusagehistory;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface AiUsageHistoryRepository
        extends JpaRepository<AiUsageHistory, Long> {


    /**
     * 특정 회원의 전체 AI 사용 이력
     *
     * 최근 사용 순
     */
    List<AiUsageHistory>
    findByMemberIdOrderByUsedAtDesc(
            Long memberId
    );


    /**
     * 특정 회원의 특정 기간 AI 사용 횟수
     *
     * 월별 무료 사용 횟수 계산에 사용
     */
    long countByMemberIdAndUsedAtGreaterThanEqualAndUsedAtLessThan(
            Long memberId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    /**
     * 특정 회원의 특정 기간 사용 이력
     */
    List<AiUsageHistory>
    findByMemberIdAndUsedAtGreaterThanEqualAndUsedAtLessThanOrderByUsedAtDesc(
            Long memberId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}