package dev.jpa.team1.auth;

import dev.jpa.team1.common.ApiResponse;
import dev.jpa.team1.member.login.LoginRequestDto;
import dev.jpa.team1.member.login.SignupRequestDto;
import dev.jpa.team1.member.login.TokenReissueRequestDto;
import dev.jpa.team1.member.login.TokenResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signup(
            @Valid @RequestBody SignupRequestDto requestDto
    ) {

        Long memberId =
                authService.signup(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(memberId)
                );
    }


    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto
    ) {

        TokenResponseDto tokenResponse =
                authService.login(requestDto);

        return ResponseEntity.ok(
                ApiResponse.success(tokenResponse)
        );
    }


    // 3. 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponseDto>> reissue(
            @Valid @RequestBody TokenReissueRequestDto requestDto
    ) {

        TokenResponseDto tokenResponse =
                authService.reissue(requestDto);

        return ResponseEntity.ok(
                ApiResponse.success(tokenResponse)
        );
    }


    // 4. 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody TokenReissueRequestDto requestDto
    ) {

        authService.logout(
                requestDto.getRefreshToken()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "성공적으로 로그아웃되었습니다."
                )
        );
    }
}