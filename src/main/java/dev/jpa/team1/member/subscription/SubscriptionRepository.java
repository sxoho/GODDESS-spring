package dev.jpa.team1.member.subscription;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {


    /**
     * 회원 구독 이력
     *
     * 최신순
     */
    List<Subscription>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );


    /**
     * 현재 활성 Premium 구독 조회
     */
    Optional<Subscription>
    findFirstByMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanOrderByEndDateDesc(
            Long memberId,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    /**
     * 현재 Premium 구독 존재 여부
     */
    boolean
    existsByMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThan(
            Long memberId,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    /**
     * 만료 시간이 지난 ACTIVE 구독
     */
    List<Subscription>
    findByStatusAndEndDateLessThanEqual(
            String status,
            LocalDateTime now
    );
}