package dev.jpa.team1.member.mypage;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.member.member.MemberResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;


    /**
     * 마이페이지 회원 정보 조회
     */
    @Transactional(readOnly = true)
    public MemberResponseDto findMember(
            Long memberId
    ) {

        Member member =
                memberRepository
                        .findById(
                                memberId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원 정보를 찾을 수 없습니다. id="
                                                + memberId
                                )
                        );


        return MemberResponseDto.from(
                member
        );
    }


    /**
     * 마이페이지 아이디 중복 확인
     *
     * 현재 회원 제외
     */
    @Transactional(readOnly = true)
    public boolean checkLoginId(
            Long memberId,
            String loginId
    ) {

        return memberRepository
                .existsByLoginIdAndIdNot(
                        loginId,
                        memberId
                );
    }


    /**
     * 마이페이지 닉네임 중복 확인
     *
     * 현재 회원 제외
     */
    @Transactional(readOnly = true)
    public boolean checkNickname(
            Long memberId,
            String nickname
    ) {

        return memberRepository
                .existsByNicknameAndIdNot(
                        nickname,
                        memberId
                );
    }


    /**
     * 마이페이지 회원 정보 수정
     *
     * 수정 가능:
     * 아이디
     * 닉네임
     * 우편번호
     * 기본 주소
     * 상세 주소
     */
    @Transactional
    public MemberResponseDto update(
            Long memberId,
            MyPageUpdateRequestDto requestDto
    ) {

        Member member =
                memberRepository
                        .findById(
                                memberId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원 정보를 찾을 수 없습니다. id="
                                                + memberId
                                )
                        );


        /**
         * 아이디 필수
         */
        if (
                requestDto.getLoginId() == null ||
                requestDto.getLoginId().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "아이디를 입력해주세요."
            );
        }


        /**
         * 닉네임 필수
         */
        if (
                requestDto.getNickname() == null ||
                requestDto.getNickname().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "닉네임을 입력해주세요."
            );
        }


        /**
         * 아이디 중복 확인
         */
        if (
                memberRepository
                        .existsByLoginIdAndIdNot(
                                requestDto.getLoginId(),
                                memberId
                        )
        ) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 아이디입니다."
            );
        }


        /**
         * 닉네임 중복 확인
         */
        if (
                memberRepository
                        .existsByNicknameAndIdNot(
                                requestDto.getNickname(),
                                memberId
                        )
        ) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 닉네임입니다."
            );
        }


        /**
         * Member Entity 수정
         */
        member.myPageUpdate(
                requestDto.getLoginId(),
                requestDto.getNickname(),
                requestDto.getZipcode(),
                requestDto.getAddress1(),
                requestDto.getAddress2()
        );


        return MemberResponseDto.from(
                member
        );
    }
    
    /**
     * 마이페이지 비밀번호 변경
     *
     * 현재 비밀번호 확인 후
     * 새로운 비밀번호로 변경
     */
    @Transactional
    public void updatePassword(
            Long memberId,
            MyPagePasswordUpdateRequestDto requestDto
    ) {

        /**
         * 회원 조회
         */
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원을 찾을 수 없습니다."
                                )
                        );


        String currentPassword =
                requestDto.getCurrentPassword();

        String newPassword =
                requestDto.getNewPassword();


        /**
         * 현재 비밀번호 입력 확인
         */
        if (
                currentPassword == null ||
                currentPassword.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "현재 비밀번호를 입력해주세요."
            );
        }


        /**
         * 새 비밀번호 입력 확인
         */
        if (
                newPassword == null ||
                newPassword.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "새 비밀번호를 입력해주세요."
            );
        }


        /**
         * 현재 비밀번호 확인
         */
        if (
                !member
                        .getPassword()
                        .equals(currentPassword)
        ) {

            throw new IllegalArgumentException(
                    "현재 비밀번호가 일치하지 않습니다."
            );
        }


        /**
         * 현재 비밀번호와
         * 새 비밀번호가 같은지 확인
         */
        if (
                currentPassword.equals(
                        newPassword
                )
        ) {

            throw new IllegalArgumentException(
                    "현재 비밀번호와 다른 비밀번호를 입력해주세요."
            );
        }


        /**
         * 비밀번호 변경
         */
        member.passwordUpdate(
                newPassword
        );
    }
}