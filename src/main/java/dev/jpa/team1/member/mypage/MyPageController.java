package dev.jpa.team1.member.mypage;

import dev.jpa.team1.member.member.MemberResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService service;


    /**
     * 마이페이지 회원 정보 조회
     */
    @GetMapping("/{memberId}")
    public MemberResponseDto findMember(
            @PathVariable("memberId")
            Long memberId
    ) {

        return service.findMember(
                memberId
        );
    }


    /**
     * 마이페이지 아이디 중복 확인
     */
    @GetMapping("/check_id")
    public boolean checkLoginId(
            @RequestParam("memberId")
            Long memberId,

            @RequestParam("loginId")
            String loginId
    ) {

        return service.checkLoginId(
                memberId,
                loginId
        );
    }


    /**
     * 마이페이지 닉네임 중복 확인
     */
    @GetMapping("/check_nickname")
    public boolean checkNickname(
            @RequestParam("memberId")
            Long memberId,

            @RequestParam("nickname")
            String nickname
    ) {

        return service.checkNickname(
                memberId,
                nickname
        );
    }


    /**
     * 마이페이지 회원 정보 수정
     */
    @PutMapping("/{memberId}")
    public MemberResponseDto update(
            @PathVariable("memberId")
            Long memberId,

            @RequestBody
            MyPageUpdateRequestDto requestDto
    ) {

        return service.update(
                memberId,
                requestDto
        );
    }
    
    /**
     * 마이페이지 비밀번호 변경
     */
    @PutMapping("/{memberId}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable("memberId")
            Long memberId,

            @RequestBody
            MyPagePasswordUpdateRequestDto requestDto
    ) {

        try {

            service.updatePassword(
                    memberId,
                    requestDto
            );


            return ResponseEntity.ok(
                    "비밀번호가 변경되었습니다."
            );


        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            e.getMessage()
                    );
        }
    }
}