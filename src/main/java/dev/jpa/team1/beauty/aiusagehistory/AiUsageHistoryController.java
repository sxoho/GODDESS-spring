package dev.jpa.team1.beauty.aiusagehistory;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/beauty/ai-usage")
@RequiredArgsConstructor
public class AiUsageHistoryController {


    private final AiUsageHistoryService service;


    /**
     * 현재 회원 AI 헤어 사용 상태
     *
     * 일반회원:
     * 월 3회
     *
     * Premium:
     * 무제한
     */
    @GetMapping("/member/{memberId}/status")
    public ResponseEntity<AiUsageStatusResponseDto>
    getStatus(
            @PathVariable("memberId")
            Long memberId
    ) {

        return ResponseEntity.ok(
                service.getStatus(
                        memberId
                )
        );
    }


    /**
     * 회원 AI 사용 이력
     */
    @GetMapping("/member/{memberId}")
    public List<AiUsageHistoryResponseDto>
    findByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {

        return service
                .findByMemberId(
                        memberId
                );
    }
}