package dev.jpa.team1.member.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;


    /**
     * ACTIVE 회원 목록
     */
    @GetMapping("/find_all")
    public List<MemberResponseDto> findAll() {

        return service.findAll();
    }


    /**
     * 탈퇴 회원 목록
     *
     * INACTIVE / UNACTIVE / WITHDRAWN
     */
    @GetMapping("/find_withdrawn")
    public List<MemberResponseDto> findWithdrawn() {

        return service.findWithdrawn();
    }


    /**
     * 아이디 중복 확인
     */
    @GetMapping("/check_id")
    public boolean checkId(
            @RequestParam("loginId")
            String loginId
    ) {

        return service.checkId(
                loginId
        );
    }


    /**
     * 닉네임 중복 확인
     */
    @GetMapping("/check_nickname")
    public boolean checkNickname(
            @RequestParam("nickname")
            String nickname
    ) {

        return service.checkNickname(
                nickname
        );
    }


    /**
     * 이메일 중복 확인
     */
    @GetMapping("/check_email")
    public boolean checkEmail(
            @RequestParam("email")
            String email
    ) {

        return service.checkEmail(
                email
        );
    }


    /**
     * 수정용 아이디 중복 확인
     *
     * 현재 회원 제외
     */
    @GetMapping("/check_id_update")
    public boolean checkLoginIdUpdate(
            @RequestParam("memberId")
            Long memberId,

            @RequestParam("loginId")
            String loginId
    ) {

        return service.checkLoginIdUpdate(
                memberId,
                loginId
        );
    }


    /**
     * 관리자 수정용 닉네임 중복 확인
     */
    @GetMapping("/check_nickname_update")
    public boolean checkNicknameUpdate(
            @RequestParam("memberId")
            Long memberId,

            @RequestParam("nickname")
            String nickname
    ) {

        return service.checkNicknameUpdate(
                memberId,
                nickname
        );
    }


    /**
     * 관리자 수정용 이메일 중복 확인
     */
    @GetMapping("/check_email_update")
    public boolean checkEmailUpdate(
            @RequestParam("memberId")
            Long memberId,

            @RequestParam("email")
            String email
    ) {

        return service.checkEmailUpdate(
                memberId,
                email
        );
    }


    /**
     * 회원 상세 조회
     */
    @GetMapping("/{id}")
    public MemberResponseDto findById(
            @PathVariable("id")
            Long id
    ) {

        return service.findById(
                id
        );
    }


    /**
     * 회원가입
     */
    @PostMapping("/save")
    public MemberResponseDto save(
            @RequestBody
            MemberRequestDto requestDto
    ) {

        return service.save(
                requestDto
        );
    }


    /**
     * 관리자 회원 수정
     */
    @PutMapping("/{id}")
    public MemberResponseDto update(
            @PathVariable("id")
            Long id,

            @RequestBody
            MemberUpdateRequestDto requestDto
    ) {

        return service.update(
                id,
                requestDto
        );
    }


    /**
     * 관리자 회원 삭제
     *
     * 실제 DB DELETE X
     */
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id")
            Long id
    ) {

        service.delete(
                id
        );
    }
    
    /**
     * 아이디 찾기
     */
    @GetMapping("/find_id")
    public String findLoginId(
            @RequestParam("name")
            String name,

            @RequestParam("email")
            String email
    ) {

        return service.findLoginId(
                name,
                email
        );
    }
    
    /**
     * 비밀번호 재설정
     */
    @PutMapping("/reset_password")
    public ResponseEntity<String> resetPassword(
            @RequestBody
            MemberPasswordResetRequestDto requestDto
    ) {

        service.resetPassword(
                requestDto
        );


        return ResponseEntity.ok(
                "비밀번호가 변경되었습니다."
        );
    }
    
}