package dev.jpa.team1.member.login;

import dev.jpa.team1.member.loginhistory.LoginHistoryService;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    private final LoginHistoryService loginHistoryService;

    /**
     * 로그인
     */
    @Transactional
    public LoginResponseDto login(
            LoginRequestDto requestDto,
            String ip,
            String device
    ) {

        /**
         * 아이디로 회원 조회
         */
        Member member =
                memberRepository
                        .findByLoginId(
                                requestDto.getLoginId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "아이디 또는 비밀번호가 일치하지 않습니다."
                                )
                        );

        /**
         * 비밀번호 확인
         */
        if (
                !member
                        .getPassword()
                        .equals(
                                requestDto.getPassword()
                        )
        ) {

            /**
             * 회원은 존재하지만
             * 비밀번호가 틀린 경우
             * 로그인 실패 기록
             */
            loginHistoryService.save(
                    member,
                    "N",
                    ip,
                    device
            );

            throw new IllegalArgumentException(
                    "아이디 또는 비밀번호가 일치하지 않습니다."
            );
        }

        /**
         * 탈퇴 / 비활성 회원
         * 로그인 차단
         */
        if (
                !"ACTIVE".equals(
                        member.getStatus()
                )
        ) {

            loginHistoryService.save(
                    member,
                    "N",
                    ip,
                    device
            );

            throw new IllegalArgumentException(
                    "로그인할 수 없는 회원입니다."
            );
        }

        /**
         * 로그인 성공 기록
         */
        loginHistoryService.save(
                member,
                "Y",
                ip,
                device
        );

        return LoginResponseDto.from(
                member
        );
    }
}