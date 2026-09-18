package dev.jpa.team1.beauty.tryon;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/beauty")
@RequiredArgsConstructor
public class BeautyTryonController {


    private final BeautyTryonService beautyTryonService;


    /**
     * AI BEAUTY 적용
     *
     * React
     * → Spring
     * → H200 11502
     *
     * POST
     * /beauty/tryon
     */
    @PostMapping("/tryon")
    public ResponseEntity<BeautyApplyResponseDto>
    applyBeauty(
            @RequestBody
            BeautyApplyRequestDto requestDto
    ) {

        return ResponseEntity.ok(
                beautyTryonService
                        .applyBeauty(
                                requestDto
                        )
        );
    }
}