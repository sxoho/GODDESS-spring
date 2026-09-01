//auth
package dev.jpa.team1.auth;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.grade.Grade;
import dev.jpa.team1.member.grade.GradeRepository;
import dev.jpa.team1.member.login.*;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final GradeRepository gradeRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 관리자 여부 확인
     *
     * GRADE_ID
     * 1 ~ 3 = ADMIN
     */
    private boolean isAdmin(Member member) {

        if (
                member.getGrade() == null ||
                member.getGrade().getId() == null
        ) {
            return false;
        }

        Long gradeId =
                member.getGrade().getId();

        return gradeId >= 1 &&
               gradeId <= 3;
    }

    // 1. 회원가입 로직
    @Transactional
    public Long signup(
            SignupRequestDto requestDto
    ) {

        if (
                memberRepository.existsByLoginId(
                        requestDto.getLoginId()
                )
        ) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_LOGIN_ID
            );
        }

        if (
                memberRepository.existsByEmail(
                        requestDto.getEmail()
                )
        ) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_EMAIL
            );
        }

        /**
         * 신규 회원 기본 등급
         * GRADE_ID = 4
         */
        Grade userGrade =
                gradeRepository.findById(4L)
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.GRADE_NOT_FOUND
                                )
                        );

        Member newMember =
                Member.builder()
                        .loginId(
                                requestDto.getLoginId()
                        )
                        .password(
                                passwordEncoder.encode(
                                        requestDto.getPassword()
                                )
                        )
                        .name(
                                requestDto.getName()
                        )
                        .nickname(
                                requestDto.getNickname()
                        )
                        .email(
                                requestDto.getEmail()
                        )
                        .tel(
                                requestDto.getTel()
                        )
                        .zipcode(
                                requestDto.getZipcode()
                        )
                        .address1(
                                requestDto.getAddress1()
                        )
                        .address2(
                                requestDto.getAddress2()
                        )
                        .grade(userGrade)
                        .status("ACTIVE")
                        .build();

        Member savedMember =
                memberRepository.save(
                        newMember
                );

        return savedMember.getId();
    }

    // 2. 로그인 및 Access/Refresh Token 발급
    @Transactional
    public TokenResponseDto login(
            LoginRequestDto requestDto
    ) {

        Member member =
                memberRepository
                        .findByLoginId(
                                requestDto.getLoginId()
                        )
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                        );

        if (
                !passwordEncoder.matches(
                        requestDto.getPassword(),
                        member.getPassword()
                )
        ) {
            throw new CustomException(
                    ErrorCode.INVALID_PASSWORD
            );
        }

        String role =
                isAdmin(member)
                        ? "ROLE_ADMIN"
                        : "ROLE_USER";

        String accessToken =
                jwtTokenProvider
                        .createAccessToken(
                                member.getLoginId(),
                                role
                        );

        String refreshToken =
                jwtTokenProvider
                        .createRefreshToken(
                                member.getLoginId()
                        );

        // Refresh Token DB 저장 또는 업데이트
        RefreshToken savedRefreshToken =
                refreshTokenRepository
                        .findByLoginId(
                                member.getLoginId()
                        )
                        .orElse(
                                RefreshToken.builder()
                                        .loginId(
                                                member.getLoginId()
                                        )
                                        .token(
                                                refreshToken
                                        )
                                        .build()
                        );

        savedRefreshToken.updateToken(
                refreshToken
        );

        refreshTokenRepository.save(
                savedRefreshToken
        );

        return new TokenResponseDto(
                "Bearer",
                accessToken,
                refreshToken,
                jwtTokenProvider
                        .getAccessExpirationTime()
        );
    }

    // 3. 토큰 재발급
    @Transactional
    public TokenResponseDto reissue(
            TokenReissueRequestDto requestDto
    ) {

        String requestRefreshToken =
                requestDto.getRefreshToken();

        // 1) Refresh Token 유효성 검증
        if (
                !jwtTokenProvider.validateToken(
                        requestRefreshToken
                )
        ) {
            throw new CustomException(
                    ErrorCode.INVALID_REFRESH_TOKEN
            );
        }

        // 2) 토큰에서 loginId 추출 후 DB 조회
        String loginId =
                jwtTokenProvider.getLoginId(
                        requestRefreshToken
                );

        RefreshToken refreshTokenEntity =
                refreshTokenRepository
                        .findByLoginId(loginId)
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.REFRESH_TOKEN_NOT_FOUND
                                )
                        );

        // 3) 요청된 토큰과 DB 토큰 비교
        if (
                !refreshTokenEntity
                        .getToken()
                        .equals(
                                requestRefreshToken
                        )
        ) {
            throw new CustomException(
                    ErrorCode.INVALID_REFRESH_TOKEN
            );
        }

        // 4) 회원 정보 및 권한 조회
        Member member =
                memberRepository
                        .findByLoginId(loginId)
                        .orElseThrow(() ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                        );

        String role =
                isAdmin(member)
                        ? "ROLE_ADMIN"
                        : "ROLE_USER";

        // 5) 새로운 Access / Refresh Token 생성
        String newAccessToken =
                jwtTokenProvider
                        .createAccessToken(
                                member.getLoginId(),
                                role
                        );

        String newRefreshToken =
                jwtTokenProvider
                        .createRefreshToken(
                                member.getLoginId()
                        );

        // 6) Refresh Token 업데이트
        refreshTokenEntity.updateToken(
                newRefreshToken
        );

        return new TokenResponseDto(
                "Bearer",
                newAccessToken,
                newRefreshToken,
                jwtTokenProvider
                        .getAccessExpirationTime()
        );
    }

    // 4. 로그아웃
    @Transactional
    public void logout(String loginId) {
        refreshTokenRepository
                .deleteByLoginId(loginId);
    }
}