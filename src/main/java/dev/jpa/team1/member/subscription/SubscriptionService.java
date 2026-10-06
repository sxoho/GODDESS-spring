package dev.jpa.team1.member.subscription;


import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SubscriptionService {


    private final SubscriptionRepository subscriptionRepository;

    private final MemberRepository memberRepository;


    /**
     * 월간 Premium 가격
     */
    private static final long MONTHLY_PRICE =
            5000L;


    /**
     * 연간 Premium 가격
     */
    private static final long YEARLY_PRICE =
            40000L;


    /**
     * Premium 구독 생성
     */
    @Transactional
    public SubscriptionResponseDto subscribe(
            SubscriptionRequestDto requestDto
    ) {


        if (
                requestDto.getMemberId() == null
        ) {

            throw new IllegalArgumentException(
                    "회원 ID가 필요합니다."
            );
        }


        if (
                requestDto.getPlanType() == null
                ||
                requestDto
                        .getPlanType()
                        .isBlank()
        ) {

            throw new IllegalArgumentException(
                    "구독 플랜을 선택해주세요."
            );
        }


        String planType =
                requestDto
                        .getPlanType()
                        .trim()
                        .toUpperCase();


        if (
                !"MONTHLY".equals(
                        planType
                )
                &&
                !"YEARLY".equals(
                        planType
                )
        ) {

            throw new IllegalArgumentException(
                    "올바르지 않은 구독 플랜입니다."
            );
        }


        Member member =
                memberRepository
                        .findById(
                                requestDto.getMemberId()
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "회원을 찾을 수 없습니다."
                                        )
                        );


        /**
         * 이미 활성화된 Premium이 있는지 확인
         */
        if (
                isPremium(
                        member.getId()
                )
        ) {

            throw new IllegalStateException(
                    "이미 Premium 구독을 이용 중입니다."
            );
        }


        LocalDateTime startDate =
                LocalDateTime.now();


        LocalDateTime endDate;


        long price;


        if (
                "MONTHLY".equals(
                        planType
                )
        ) {

            price =
                    MONTHLY_PRICE;

            endDate =
                    startDate
                            .plusMonths(
                                    1
                            );

        } else {

            price =
                    YEARLY_PRICE;

            endDate =
                    startDate
                            .plusYears(
                                    1
                            );
        }


        Subscription subscription =
                Subscription
                        .builder()
                        .member(
                                member
                        )
                        .planType(
                                planType
                        )
                        .status(
                                "ACTIVE"
                        )
                        .price(
                                price
                        )
                        .startDate(
                                startDate
                        )
                        .endDate(
                                endDate
                        )
                        .build();


        subscription =
                subscriptionRepository
                        .save(
                                subscription
                        );


        return SubscriptionResponseDto.from(
                subscription
        );
    }


    /**
     * 현재 Premium 여부
     *
     * AI 헤어 횟수 제한 체크에서도 사용
     */
    @Transactional(readOnly = true)
    public boolean isPremium(
            Long memberId
    ) {


        if (
                memberId == null
        ) {

            return false;
        }


        LocalDateTime now =
                LocalDateTime.now();


        return subscriptionRepository
                .existsByMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThan(
                        memberId,
                        "ACTIVE",
                        now,
                        now
                );
    }


    /**
     * 현재 Premium 상태 조회
     */
    @Transactional(readOnly = true)
    public SubscriptionStatusResponseDto getStatus(
            Long memberId
    ) {


        if (
                memberId == null
        ) {

            throw new IllegalArgumentException(
                    "회원 ID가 필요합니다."
            );
        }


        LocalDateTime now =
                LocalDateTime.now();


        Optional<Subscription> optional =
                subscriptionRepository
                        .findFirstByMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanOrderByEndDateDesc(
                                memberId,
                                "ACTIVE",
                                now,
                                now
                        );


        if (
                optional.isEmpty()
        ) {

            return new SubscriptionStatusResponseDto(
                    memberId,
                    false,
                    null,
                    null,
                    null,
                    null
            );
        }


        Subscription subscription =
                optional.get();


        return new SubscriptionStatusResponseDto(
                memberId,
                true,
                subscription.getPlanType(),
                subscription.getStatus(),
                subscription.getStartDate(),
                subscription.getEndDate()
        );
    }


    /**
     * 회원 구독 이력
     */
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDto>
    findByMemberId(
            Long memberId
    ) {


        return subscriptionRepository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        SubscriptionResponseDto::from
                )
                .toList();
    }


    /**
     * 구독 취소
     */
    @Transactional
    public SubscriptionResponseDto cancel(
            Long subscriptionId
    ) {


        Subscription subscription =
                subscriptionRepository
                        .findById(
                                subscriptionId
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "구독 정보를 찾을 수 없습니다."
                                        )
                        );


        if (
                !"ACTIVE".equals(
                        subscription.getStatus()
                )
        ) {

            throw new IllegalStateException(
                    "현재 활성화된 구독이 아닙니다."
            );
        }


        subscription.cancel();


        return SubscriptionResponseDto.from(
                subscription
        );
    }


    /**
     * 기간이 끝난 ACTIVE 구독을
     * EXPIRED 상태로 변경
     *
     * 필요할 때 관리자 기능이나
     * 스케줄러에서 호출 가능
     */
    @Transactional
    public void expireSubscriptions() {


        LocalDateTime now =
                LocalDateTime.now();


        List<Subscription> subscriptions =
                subscriptionRepository
                        .findByStatusAndEndDateLessThanEqual(
                                "ACTIVE",
                                now
                        );


        for (
                Subscription subscription
                :
                subscriptions
        ) {

            subscription.expire();
        }
    }
}