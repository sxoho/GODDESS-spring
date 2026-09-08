package dev.jpa.team1.beauty.skindiagnosis;

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
public class SkinDiagnosisService {

    private final SkinDiagnosisResultRepository skinDiagnosisResultRepository;

    private final MemberRepository memberRepository;

    /**
     * 사용 가능한 피부 타입
     */
    private static final Set<String> VALID_SKIN_TYPES =
            Set.of(
                    "DRY",
                    "OILY",
                    "COMBINATION",
                    "SENSITIVE",
                    "NORMAL",
                    "DEHYDRATED"
            );


    /**
     * 피부 자가진단 결과 저장
     *
     * 처리 내용:
     * 1. 회원 확인
     * 2. 피부 타입 검증
     * 3. 피부 진단 이력 저장
     * 4. MEMBER의 현재 피부 타입 변경
     */
    @Transactional
    public SkinDiagnosisResponseDto saveDiagnosis(
            SkinDiagnosisRequestDto requestDto
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


        String skinType =
                requestDto.getSkinType();


        if (
                skinType == null ||
                !VALID_SKIN_TYPES.contains(
                        skinType
                )
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "올바르지 않은 피부 타입입니다."
            );
        }


        SkinDiagnosisResult result =
                SkinDiagnosisResult.builder()

                        .member(
                                member
                        )

                        .skinType(
                                skinType
                        )

                        .dryScore(
                                scoreOrZero(
                                        requestDto.getDryScore()
                                )
                        )

                        .oilyScore(
                                scoreOrZero(
                                        requestDto.getOilyScore()
                                )
                        )

                        .combinationScore(
                                scoreOrZero(
                                        requestDto.getCombinationScore()
                                )
                        )

                        .normalScore(
                                scoreOrZero(
                                        requestDto.getNormalScore()
                                )
                        )

                        .sensitiveScore(
                                scoreOrZero(
                                        requestDto.getSensitiveScore()
                                )
                        )

                        .dehydratedScore(
                                scoreOrZero(
                                        requestDto.getDehydratedScore()
                                )
                        )

                        .resultDescription(
                                requestDto.getResultDescription()
                        )

                        .build();


        SkinDiagnosisResult savedResult =
                skinDiagnosisResultRepository
                        .save(
                                result
                        );


        /**
         * 현재 회원 피부 타입 변경
         *
         * 이후 마이페이지,
         * 상품 추천,
         * 챗봇 개인화에서 사용
         */
        member.skinTypeUpdate(
                skinType
        );


        return SkinDiagnosisResponseDto.from(
                savedResult
        );
    }


    /**
     * 회원 피부 진단 이력 조회
     *
     * 최신 진단 순으로 반환
     */
    @Transactional(readOnly = true)
    public List<SkinDiagnosisResponseDto> getMemberDiagnosisHistory(
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


        return skinDiagnosisResultRepository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        SkinDiagnosisResponseDto::from
                )
                .toList();
    }


    /**
     * null 점수는 0으로 처리
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
                    "피부 진단 점수는 0보다 작을 수 없습니다."
            );
        }


        return score;
    }
}