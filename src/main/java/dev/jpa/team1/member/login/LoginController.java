package dev.jpa.team1.member.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid
            @RequestBody
            LoginRequestDto requestDto,

            HttpServletRequest request
    ) {

        /**
         * 접속 IP
         */
        String ip =
                getClientIp(
                        request
                );

        /**
         * 접속 기기 / 브라우저
         */
        String userAgent =
                request.getHeader(
                        "User-Agent"
                );

        String device =
                getDevice(
                        userAgent
                );

        LoginResponseDto response =
                loginService.login(
                        requestDto,
                        ip,
                        device
                );

        return ResponseEntity.ok(
                response
        );
    }

    /**
     * 클라이언트 IP 가져오기
     */
    private String getClientIp(
            HttpServletRequest request
    ) {

        String forwarded =
                request.getHeader(
                        "X-Forwarded-For"
                );

        if (
                forwarded != null &&
                !forwarded.isBlank()
        ) {

            return forwarded
                    .split(",")[0]
                    .trim();
        }

        return request.getRemoteAddr();
    }

    /**
     * User-Agent를
     * 보기 쉬운 기기 / 브라우저 정보로 변환
     */
    private String getDevice(
            String userAgent
    ) {

        if (
                userAgent == null ||
                userAgent.isBlank()
        ) {

            return "Unknown";
        }

        String os = "Unknown";

        String browser = "Unknown";

        /**
         * 운영체제 / 기기
         */
        if (
                userAgent.contains(
                        "Windows"
                )
        ) {

            os = "Windows";

        } else if (
                userAgent.contains(
                        "iPhone"
                )
        ) {

            os = "iPhone";

        } else if (
                userAgent.contains(
                        "iPad"
                )
        ) {

            os = "iPad";

        } else if (
                userAgent.contains(
                        "Android"
                )
        ) {

            os = "Android";

        } else if (
                userAgent.contains(
                        "Macintosh"
                )
        ) {

            os = "Mac";

        } else if (
                userAgent.contains(
                        "Linux"
                )
        ) {

            os = "Linux";
        }

        /**
         * 브라우저
         *
         * Edge / Chrome 순서 중요
         */
        if (
                userAgent.contains(
                        "Edg/"
                )
        ) {

            browser = "Edge";

        } else if (
                userAgent.contains(
                        "Chrome/"
                )
        ) {

            browser = "Chrome";

        } else if (
                userAgent.contains(
                        "Firefox/"
                )
        ) {

            browser = "Firefox";

        } else if (
                userAgent.contains(
                        "Safari/"
                )
        ) {

            browser = "Safari";
        }

        return browser
                + " / "
                + os;
    }
}