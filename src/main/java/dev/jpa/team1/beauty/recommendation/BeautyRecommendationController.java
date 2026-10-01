package dev.jpa.team1.beauty.recommendation;

import dev.jpa.team1.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beauty_recommendation")
@RequiredArgsConstructor
public class BeautyRecommendationController {

    private final BeautyRecommendationService beautyRecommendationService;

    /**
     * 추천 화면에서 회원의 퍼스널컬러/피부타입 불러오기
     *
     * GET /beauty_recommendation/member/{memberId}/profile
     */
    @GetMapping("/member/{memberId}/profile")
    public ResponseEntity<ApiResponse<BeautyRecommendationProfileDto>>
    getMemberProfile(
            @PathVariable("memberId") Long memberId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        beautyRecommendationService.getMemberProfile(memberId)
                )
        );
    }

    /**
     * 뷰티 상품 추천 생성
     *
     * POST /beauty_recommendation
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BeautyRecommendationResponseDto>>
    createRecommendation(
            @RequestBody BeautyRecommendationRequestDto requestDto
    ) {
        BeautyRecommendationResponseDto response =
                beautyRecommendationService.createRecommendation(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 회원별 추천 이력 조회
     *
     * GET /beauty_recommendation/member/{memberId}
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<BeautyRecommendationResponseDto>>>
    getMemberHistory(
            @PathVariable("memberId") Long memberId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        beautyRecommendationService.getMemberHistory(memberId)
                )
        );
    }

    /**
     * 추천 이력 한 건 조회
     *
     * GET /beauty_recommendation/{recommendationId}
     */
    @GetMapping("/{recommendationId}")
    public ResponseEntity<ApiResponse<BeautyRecommendationResponseDto>>
    getRecommendation(
            @PathVariable("recommendationId") Long recommendationId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        beautyRecommendationService.getRecommendation(recommendationId)
                )
        );
    }
}
