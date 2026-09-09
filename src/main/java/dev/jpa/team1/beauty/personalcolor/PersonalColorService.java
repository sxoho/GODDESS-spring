package dev.jpa.team1.beauty.personalcolor;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonalColorService {

    private final PersonalColorResultRepository personalColorResultRepository;

    private final MemberRepository memberRepository;


    /**
     * 저장 가능한 퍼스널컬러 결과
     */
    private static final Set<String> VALID_PERSONAL_COLORS =
            Set.of(
                    "SPRING_WARM",
                    "SUMMER_COOL",
                    "AUTUMN_WARM",
                    "WINTER_COOL"
            );


    /**
     * 퍼스널컬러 자가진단 결과 저장
     *
     * 1. 회원 확인
     * 2. 퍼스널컬러 값 확인
     * 3. PERSONAL_COLOR_RESULT 진단 이력 저장
     * 4. MEMBER.PERSONAL_COLOR 최신 결과로 변경
     */
    @Transactional
    public PersonalColorResponseDto saveDiagnosis(
            PersonalColorRequestDto requestDto
    ) {

        if (
                requestDto.getMemberId() == null
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "회원 번호가 필요합니다."
            );
        }


        Member member =
                memberRepository
                        .findById(
                                requestDto.getMemberId()
                        )
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.MEMBER_NOT_FOUND
                                        )
                        );


        String personalColor =
                requestDto.getPersonalColor();


        if (
                personalColor == null ||
                !VALID_PERSONAL_COLORS.contains(
                        personalColor
                )
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "올바르지 않은 퍼스널컬러입니다."
            );
        }


        PersonalColorResult result =
                PersonalColorResult.builder()

                        .member(
                                member
                        )

                        .personalColor(
                                personalColor
                        )

                        .springScore(
                                scoreOrZero(
                                        requestDto.getSpringScore()
                                )
                        )

                        .summerScore(
                                scoreOrZero(
                                        requestDto.getSummerScore()
                                )
                        )

                        .autumnScore(
                                scoreOrZero(
                                        requestDto.getAutumnScore()
                                )
                        )

                        .winterScore(
                                scoreOrZero(
                                        requestDto.getWinterScore()
                                )
                        )

                        .resultDescription(
                                requestDto.getResultDescription()
                        )

                        .build();


        PersonalColorResult savedResult =
                personalColorResultRepository
                        .save(
                                result
                        );


        /**
         * MEMBER 테이블의 현재 퍼스널컬러도 함께 변경
         */
        member.personalColorUpdate(
                personalColor
        );


        return PersonalColorResponseDto.from(
                savedResult
        );
    }


    /**
     * 회원별 퍼스널컬러 진단 이력 조회
     */
    @Transactional(readOnly = true)
    public List<PersonalColorResponseDto> getMemberDiagnosisHistory(
            Long memberId
    ) {

        if (
                !memberRepository.existsById(
                        memberId
                )
        ) {

            throw new CustomException(
                    ErrorCode.MEMBER_NOT_FOUND
            );
        }


        return personalColorResultRepository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        PersonalColorResponseDto::from
                )
                .toList();
    }


    /**
     * null 점수는 0으로 저장
     */
    private Integer scoreOrZero(
            Integer score
    ) {

        if (
                score == null
        ) {
            return 0;
        }


        if (
                score < 0
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "퍼스널컬러 진단 점수는 0보다 작을 수 없습니다."
            );
        }


        return score;
    }
}