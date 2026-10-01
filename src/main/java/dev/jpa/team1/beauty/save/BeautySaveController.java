package dev.jpa.team1.beauty.save;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/beauty")
@RequiredArgsConstructor
public class BeautySaveController {


    private final BeautySaveService beautySaveService;


    /**
     * 결과 저장
     *
     * POST /beauty/save
     */
    @PostMapping("/save")
    public ResponseEntity<BeautySaveResponseDto>
    saveResult(
            @RequestBody
            BeautySaveRequestDto requestDto
    ) {

        return ResponseEntity.ok(
            beautySaveService
                .saveResult(
                    requestDto
                )
        );
    }


    /**
     * MY HISTORY
     *
     * GET /beauty/save/member/{memberId}
     */
    @GetMapping(
        "/save/member/{memberId}"
    )
    public ResponseEntity<
            List<BeautySaveHistoryDto>
    >
    getMemberHistory(
            @PathVariable("memberId")
            Long memberId
    ) {

        return ResponseEntity.ok(
            beautySaveService
                .getMemberHistory(
                    memberId
                )
        );
    }


    /**
     * 저장된 MY HISTORY 삭제
     *
     * DELETE
     * /beauty/save/{beautySaveId}/member/{memberId}
     */
    @DeleteMapping(
        "/save/{beautySaveId}/member/{memberId}"
    )
    public ResponseEntity<Void>
    deleteSavedHistory(
            @PathVariable("beautySaveId")
            Long beautySaveId,

            @PathVariable("memberId")
            Long memberId
    ) {

        beautySaveService
            .deleteSavedHistory(
                beautySaveId,
                memberId
            );


        return ResponseEntity
                .noContent()
                .build();
    }
}