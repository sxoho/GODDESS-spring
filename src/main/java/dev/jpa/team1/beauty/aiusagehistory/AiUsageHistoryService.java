package dev.jpa.team1.beauty.aiusagehistory;


import dev.jpa.team1.member.member.Member;

import dev.jpa.team1.member.subscription.SubscriptionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AiUsageHistoryService {


    private final AiUsageHistoryRepository repository;

    private final SubscriptionService subscriptionService;


    /**
     * 일반 회원
     * 월 무료 헤어 AI 사용 횟수
     */
    private static final int MONTHLY_FREE_LIMIT =
            3;


    /**
     * AI BEAUTY 실행 전에
     * 헤어 무료 사용 가능 여부 확인
     */
    @Transactional(readOnly = true)
    public void validateHairUsageAvailable(
            Long memberId,
            String hairStyle,
            String hairColor
    ) {


        /**
         * 헤어 기능이 포함되지 않았다면
         * 횟수 제한과 관계 없음
         */
        if (
                !hasHairSelection(
                        hairStyle,
                        hairColor
                )
        ) {

            return;
        }


        /**
         * Premium 회원은 무제한
         */
        if (
                subscriptionService
                        .isPremium(
                                memberId
                        )
        ) {

            return;
        }


        long usedCount =
                getCurrentMonthUsedCount(
                        memberId
                );


        if (
                usedCount
                >=
                MONTHLY_FREE_LIMIT
        ) {

            throw new IllegalStateException(
                    "이번 달 무료 헤어 AI 체험 3회를 모두 사용했습니다. Premium 구독 시 무제한으로 이용할 수 있습니다."
            );
        }
    }


    /**
     * AI BEAUTY 성공 후
     * 헤어 사용 이력 저장
     */
    @Transactional
    public void saveHairUsage(
            Member member,
            String hairStyle,
            String hairColor
    ) {


        if (
                member == null
        ) {

            throw new IllegalArgumentException(
                    "회원 정보가 필요합니다."
            );
        }


        /**
         * 헤어 기능이 없는 경우
         * 사용 이력 저장하지 않음
         */
        if (
                !hasHairSelection(
                        hairStyle,
                        hairColor
                )
        ) {

            return;
        }


        /**
         * Premium은 무제한이므로
         * 무료 사용 이력에는 저장하지 않는다.
         */
        if (
                subscriptionService
                        .isPremium(
                                member.getId()
                        )
        ) {

            return;
        }


        String usageType =
                resolveUsageType(
                        hairStyle,
                        hairColor
                );


        AiUsageHistory history =
                AiUsageHistory
                        .builder()
                        .member(
                                member
                        )
                        .usageType(
                                usageType
                        )
                        .build();


        repository.save(
                history
        );
    }


    /**
     * 이번 달 사용 횟수
     */
    @Transactional(readOnly = true)
    public long getCurrentMonthUsedCount(
            Long memberId
    ) {


        LocalDate today =
                LocalDate.now();


        LocalDateTime startDate =
                today
                        .withDayOfMonth(
                                1
                        )
                        .atStartOfDay();


        LocalDateTime endDate =
                startDate
                        .plusMonths(
                                1
                        );


        return repository
                .countByMemberIdAndUsedAtGreaterThanEqualAndUsedAtLessThan(
                        memberId,
                        startDate,
                        endDate
                );
    }


    /**
     * 현재 회원 AI 사용 상태 조회
     */
    @Transactional(readOnly = true)
    public AiUsageStatusResponseDto getStatus(
            Long memberId
    ) {


        boolean premium =
                subscriptionService
                        .isPremium(
                                memberId
                        );


        long usedCount =
                getCurrentMonthUsedCount(
                        memberId
                );


        Integer remainingCount;


        if (
                premium
        ) {

            /**
             * Premium은 무제한이므로
             * 남은 횟수 개념 없음
             */
            remainingCount =
                    null;

        } else {

            remainingCount =
                    Math.max(
                            MONTHLY_FREE_LIMIT
                            -
                            (int) usedCount,
                            0
                    );
        }


        return new AiUsageStatusResponseDto(
                memberId,
                premium,
                premium,
                MONTHLY_FREE_LIMIT,
                usedCount,
                remainingCount
        );
    }


    /**
     * 회원 전체 AI 사용 이력
     */
    @Transactional(readOnly = true)
    public List<AiUsageHistoryResponseDto>
    findByMemberId(
            Long memberId
    ) {


        return repository
                .findByMemberIdOrderByUsedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        AiUsageHistoryResponseDto::from
                )
                .toList();
    }


    /**
     * 헤어 선택 여부
     */
    private boolean hasHairSelection(
            String hairStyle,
            String hairColor
    ) {

        return hasText(
                hairStyle
        )
        ||
        hasText(
                hairColor
        );
    }


    /**
     * 사용 종류 결정
     *
     * HAIR_STYLE
     * HAIR_COLOR
     * HAIR_BOTH
     */
    private String resolveUsageType(
            String hairStyle,
            String hairColor
    ) {


        boolean hasHairStyle =
                hasText(
                        hairStyle
                );


        boolean hasHairColor =
                hasText(
                        hairColor
                );


        if (
                hasHairStyle
                &&
                hasHairColor
        ) {

            return "HAIR_BOTH";
        }


        if (
                hasHairStyle
        ) {

            return "HAIR_STYLE";
        }


        if (
                hasHairColor
        ) {

            return "HAIR_COLOR";
        }


        throw new IllegalArgumentException(
                "헤어 사용 종류를 확인할 수 없습니다."
        );
    }


    /**
     * null / 공백 검사
     */
    private boolean hasText(
            String value
    ) {

        return value != null
                &&
                !value.isBlank();
    }
}