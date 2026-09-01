package dev.jpa.team1.member.loginhistory;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/login-history")
@RequiredArgsConstructor
public class LoginHistoryController {

    private final LoginHistoryService service;


    /**
     * 회원별 로그인 내역 조회
     *
     * 관리자 정보보기 화면과
     * 사용자 My Page에서 사용 가능
     */
    @GetMapping("/member/{memberId}")
    public List<LoginHistoryResponseDto>
    findByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {

        return service.findByMemberId(
                memberId
        );
    }


    /**
     * 로그아웃 시간 저장
     */
    @PutMapping("/logout/{memberId}")
    public ResponseEntity<String>
    logout(
            @PathVariable("memberId")
            Long memberId
    ) {

        try {

            service.logout(
                    memberId
            );


            return ResponseEntity.ok(
                    "로그아웃 시간이 저장되었습니다."
            );


        } catch (
                IllegalArgumentException e
        ) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            e.getMessage()
                    );
        }
    }
}