package dev.jpa.team1.member.subscription;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {


    private final SubscriptionService subscriptionService;


    /**
     * Premium 구독 등록
     */
    @PostMapping
    public ResponseEntity<SubscriptionResponseDto>
    subscribe(
            @RequestBody
            SubscriptionRequestDto requestDto
    ) {

        return ResponseEntity.ok(
                subscriptionService
                        .subscribe(
                                requestDto
                        )
        );
    }


    /**
     * 현재 Premium 상태 조회
     */
    @GetMapping("/member/{memberId}/status")
    public ResponseEntity<SubscriptionStatusResponseDto>
    getStatus(
            @PathVariable("memberId")
            Long memberId
    ) {

        return ResponseEntity.ok(
                subscriptionService
                        .getStatus(
                                memberId
                        )
        );
    }


    /**
     * 회원 구독 이력 조회
     */
    @GetMapping("/member/{memberId}")
    public List<SubscriptionResponseDto>
    findByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {

        return subscriptionService
                .findByMemberId(
                        memberId
                );
    }


    /**
     * Premium 구독 취소
     */
    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponseDto>
    cancel(
            @PathVariable("subscriptionId")
            Long subscriptionId
    ) {

        return ResponseEntity.ok(
                subscriptionService
                        .cancel(
                                subscriptionId
                        )
        );
    }
}