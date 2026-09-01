package dev.jpa.team1.member.memberphoto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/member_photo")
@RequiredArgsConstructor
public class MemberPhotoController {

    private final MemberPhotoService memberPhotoService;


    /**
     * 회원 대표 사진 조회
     *
     * GET
     * /member_photo/member/7/main
     */
    @GetMapping("/member/{memberId}/main")
    public ResponseEntity<?> findMainPhoto(
            @PathVariable("memberId") Long memberId
    ) {

        return memberPhotoService
                .findMainPhoto(memberId)
                .<ResponseEntity<?>>map(
                        ResponseEntity::ok
                )
                .orElseGet(
                        () ->
                                ResponseEntity
                                        .noContent()
                                        .build()
                );
    }


    /**
     * 회원 사진 전체 조회
     *
     * GET
     * /member_photo/member/7
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MemberPhotoResponseDto>>
    findByMemberId(
            @PathVariable("memberId") Long memberId
    ) {

        return ResponseEntity.ok(
                memberPhotoService
                        .findByMemberId(memberId)
        );
    }


    /**
     * 회원 사진 AI 정면 검사
     *
     * 실제 사진 저장 X
     *
     * POST
     * /member_photo/check
     */
    @PostMapping("/check")
    public ResponseEntity<MemberPhotoAiCheckDto>
    checkPhoto(
            @RequestParam("file")
            MultipartFile file
    ) {

        return ResponseEntity.ok(
                memberPhotoService
                        .checkPhoto(file)
        );
    }


    /**
     * 회원 사진 등록 / 변경
     *
     * POST
     * /member_photo/member/7
     */
    @PostMapping("/member/{memberId}")
    public ResponseEntity<MemberPhotoResponseDto>
    upload(
            @PathVariable("memberId") Long memberId,

            @RequestParam("file")
            MultipartFile file
    ) throws IOException {

        return ResponseEntity.ok(
                memberPhotoService
                        .upload(
                                memberId,
                                file
                        )
        );
    }


    /**
     * 회원 사진 사용 중지
     *
     * PATCH
     * /member_photo/1/hide
     */
    @PatchMapping("/{memberPhotoId}/hide")
    public ResponseEntity<Void>
    hidePhoto(
            @PathVariable("memberPhotoId")
            Long memberPhotoId
    ) {

        memberPhotoService
                .hidePhoto(memberPhotoId);


        return ResponseEntity
                .noContent()
                .build();
    }
}