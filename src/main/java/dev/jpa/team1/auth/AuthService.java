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
     * 회원 등급에 따른 권한 반환
     *
     * GRADE_ID
     * 1 = USER
     * 2 = GOLD
     * 3 = VIP
     * 4 = ADMIN
     */
    private String getRole(Member member) {
        if (member.getGrade() == null || member.getGrade().getId() == null) {
            return "ROLE_USER";
        }

        Long gradeId = member.getGrade().getId();

        if (gradeId == 2L) {
            return "ROLE_GOLD";
        } else if (gradeId == 3L) {
            return "ROLE_VIP";
        } else if (gradeId == 4L) {
            return "ROLE_ADMIN";
        }

        return "ROLE_USER";
    }

    // 1. 회원가입 로직
    @Transactional
    public Long signup(SignupRequestDto requestDto) {

        if (memberRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        /**
         * 신규 회원 기본 등급
         * GRADE_ID = 1 (USER)
         */
        Grade userGrade = gradeRepository.findById(1L)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.GRADE_NOT_FOUND)
                );

        Member newMember = Member.builder()
                .loginId(requestDto.getLoginId())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .tel(requestDto.getTel())
                .zipcode(requestDto.getZipcode())
                .address1(requestDto.getAddress1())
                .address2(requestDto.getAddress2())
                .grade(userGrade)
                .status("ACTIVE")
                .build();

        Member savedMember = memberRepository.save(newMember);

        return savedMember.getId();
    }

    // 2. 로그인 및 Access/Refresh Token 발급
    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto) {

        Member member = memberRepository
                .findByLoginId(requestDto.getLoginId())
                .orElseThrow(() ->
                        new CustomException(ErrorCode.MEMBER_NOT_FOUND)
                );

        if (!passwordEncoder.matches(
                requestDto.getPassword(),
                member.getPassword()
        )) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String role = getRole(member);

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getLoginId(),
                role
        );

        String refreshToken = jwtTokenProvider.createRefreshToken(
                member.getLoginId()
        );

        // Refresh Token DB 저장 또는 업데이트
        RefreshToken savedRefreshToken = refreshTokenRepository
                .findByLoginId(member.getLoginId())
                .orElse(
                        RefreshToken.builder()
                                .loginId(member.getLoginId())
                                .token(refreshToken)
                                .build()
                );

        savedRefreshToken.updateToken(refreshToken);
        refreshTokenRepository.save(savedRefreshToken);

        return new TokenResponseDto(
                "Bearer",
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessExpirationTime()
        );
    }

    // 3. 토큰 재발급
    @Transactional
    public TokenResponseDto reissue(TokenReissueRequestDto requestDto) {

        String requestRefreshToken = requestDto.getRefreshToken();

        // 1) Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2) 토큰에서 loginId 추출 후 DB 조회
        String loginId = jwtTokenProvider.getLoginId(requestRefreshToken);

        RefreshToken refreshTokenEntity = refreshTokenRepository
                .findByLoginId(loginId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
                );

        // 3) 요청된 토큰과 DB 토큰 비교
        if (!refreshTokenEntity.getToken().equals(requestRefreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 4) 회원 정보 및 권한 조회
        Member member = memberRepository
                .findByLoginId(loginId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.MEMBER_NOT_FOUND)
                );

        String role = getRole(member);

        // 5) 새로운 Access / Refresh Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(
                member.getLoginId(),
                role
        );

        String newRefreshToken = jwtTokenProvider.createRefreshToken(
                member.getLoginId()
        );

        // 6) Refresh Token 업데이트
        refreshTokenEntity.updateToken(newRefreshToken);

        return new TokenResponseDto(
                "Bearer",
                newAccessToken,
                newRefreshToken,
                jwtTokenProvider.getAccessExpirationTime()
        );
    }

    // 4. 로그아웃
    @Transactional
    public void logout(String loginId) {
        refreshTokenRepository.deleteByLoginId(loginId);
    }
}