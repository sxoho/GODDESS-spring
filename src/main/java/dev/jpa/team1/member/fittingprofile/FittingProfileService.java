package dev.jpa.team1.member.fittingprofile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;


/**
 * 피팅 프로필 Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FittingProfileService {

    private final FittingProfileRepository
        fittingProfileRepository;

    private final MemberRepository
        memberRepository;


    /**
     * 회원의 피팅 프로필 존재 여부
     *
     * AI FITTING 진입 시
     * 프로필 등록 여부 확인에 사용한다.
     */
    public boolean existsByMemberId(
        Long memberId
    ) {

        return fittingProfileRepository
            .existsByMemberId(
                memberId
            );
    }


    /**
     * 회원 피팅 프로필 조회
     */
    public FittingProfileResponseDto findByMemberId(
        Long memberId
    ) {

        FittingProfile fittingProfile =
            fittingProfileRepository
                .findByMemberId(
                    memberId
                )
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "등록된 피팅 프로필이 없습니다. memberId="
                                + memberId
                        )
                );


        return FittingProfileResponseDto
            .from(
                fittingProfile
            );
    }


    /**
     * 피팅 프로필 최초 등록
     */
    @Transactional
    public FittingProfileResponseDto save(
        FittingProfileRequestDto request
    ) {

        /**
         * 동일 회원이 피팅 프로필을
         * 여러 개 등록하는 것을 방지한다.
         *
         * 현재 AI FITTING에서는
         * 회원 1명당 피팅 프로필 1개를 사용한다.
         */
        if (
            fittingProfileRepository
                .existsByMemberId(
                    request.getMemberId()
                )
        ) {

            throw new IllegalArgumentException(
                "이미 등록된 피팅 프로필이 있습니다."
            );
        }


        /**
         * 회원 조회
         */
        Member member =
            memberRepository
                .findById(
                    request.getMemberId()
                )
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "회원을 찾을 수 없습니다. memberId="
                                + request.getMemberId()
                        )
                );


        /**
         * 피팅 프로필 생성
         *
         * height는 RequestDto에서 BigDecimal 타입으로 받아
         * Oracle NUMBER(5,1) 컬럼에 그대로 저장한다.
         */
        FittingProfile fittingProfile =
            FittingProfile
                .builder()

                .member(
                    member
                )

                .height(
                    request.getHeight()
                )

                .bodyType(
                    normalize(
                        request.getBodyType()
                    )
                )

                .topSize(
                    normalize(
                        request.getTopSize()
                    )
                )

                .bottomSize(
                    normalize(
                        request.getBottomSize()
                    )
                )

                .preferredFit(
                    normalize(
                        request.getPreferredFit()
                    )
                )

                .build();


        /**
         * 피팅 프로필 저장
         */
        FittingProfile saved =
            fittingProfileRepository
                .save(
                    fittingProfile
                );


        /**
         * Entity -> ResponseDto 변환 후 반환
         */
        return FittingProfileResponseDto
            .from(
                saved
            );
    }


    /**
     * 피팅 프로필 수정
     */
    @Transactional
    public FittingProfileResponseDto update(
        Long memberId,
        FittingProfileRequestDto request
    ) {

        /**
         * URL의 회원 번호와
         * 요청 데이터의 회원 번호가 같은지 확인한다.
         */
        if (
            !memberId.equals(
                request.getMemberId()
            )
        ) {

            throw new IllegalArgumentException(
                "회원 정보가 일치하지 않습니다."
            );
        }


        /**
         * 기존 피팅 프로필 조회
         */
        FittingProfile fittingProfile =
            fittingProfileRepository
                .findByMemberId(
                    memberId
                )
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "등록된 피팅 프로필이 없습니다. memberId="
                                + memberId
                        )
                );


        /**
         * 피팅 프로필 정보 수정
         *
         * height는 BigDecimal 값을 그대로 전달한다.
         */
        fittingProfile.update(

            request.getHeight(),

            normalize(
                request.getBodyType()
            ),

            normalize(
                request.getTopSize()
            ),

            normalize(
                request.getBottomSize()
            ),

            normalize(
                request.getPreferredFit()
            )
        );


        /**
         * @Transactional 상태이므로
         * 별도의 save() 호출 없이 변경 감지로 UPDATE 된다.
         */
        return FittingProfileResponseDto
            .from(
                fittingProfile
            );
    }


    /**
     * 문자열 공통 정리
     *
     * 앞뒤 공백 제거 후
     * DB에 영문 대문자로 통일해서 저장한다.
     *
     * 예:
     * normal -> NORMAL
     * relaxed -> RELAXED
     */
    private String normalize(
        String value
    ) {

        if (
            value == null
        ) {

            return null;
        }


        return value
            .trim()
            .toUpperCase();
    }
}