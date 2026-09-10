package dev.jpa.team1.member.login;

import dev.jpa.team1.member.loginhistory.LoginHistoryService;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    private final LoginHistoryService loginHistoryService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Refresh Token 저장
     */
    private final RefreshTokenRepository refreshTokenRepository;


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
         * 회원 조회
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
         *
         * 현재 GODDESS 기존 로그인 방식 유지
         */
        if (
                !member
                        .getPassword()
                        .equals(
                                requestDto.getPassword()
                        )
        ) {

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
         * 탈퇴 / 비활성 회원 로그인 차단
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


        /**
         * 회원 등급별 권한
         *
         * 1 = USER
         * 2 = GOLD
         * 3 = VIP
         * 4 = ADMIN
         */
        String role =
                "ROLE_USER";


        if (
                member.getGrade() != null &&
                member.getGrade().getId() != null
        ) {

            Long gradeId =
                    member.getGrade().getId();


            if (gradeId == 2L) {
                role =
                        "ROLE_GOLD";

            } else if (gradeId == 3L) {
                role =
                        "ROLE_VIP";

            } else if (gradeId == 4L) {
                role =
                        "ROLE_ADMIN";
            }
        }


        /**
         * Access Token 생성
         */
        String accessToken =
                jwtTokenProvider
                        .createAccessToken(
                                member.getLoginId(),
                                role
                        );


        /**
         * Refresh Token 생성
         */
        String refreshToken =
                jwtTokenProvider
                        .createRefreshToken(
                                member.getLoginId()
                        );


        /**
         * 회원당 Refresh Token 1개 유지
         * 기존 토큰이 있으면 갱신하고,
         * 없으면 새로 저장한다.
         */
        RefreshToken refreshTokenEntity =
                refreshTokenRepository
                        .findByLoginId(
                                member.getLoginId()
                        )
                        .orElseGet(() ->
                                RefreshToken
                                        .builder()
                                        .loginId(
                                                member.getLoginId()
                                        )
                                        .token(
                                                refreshToken
                                        )
                                        .build()
                        );


        refreshTokenEntity.updateToken(
                refreshToken
        );


        refreshTokenRepository.save(
                refreshTokenEntity
        );


        /**
         * 로그인 결과 반환
         */
        return LoginResponseDto.from(
                member,
                accessToken,
                refreshToken
        );
    }
}