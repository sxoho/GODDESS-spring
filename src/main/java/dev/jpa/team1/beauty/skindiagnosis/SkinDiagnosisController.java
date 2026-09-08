package dev.jpa.team1.beauty.skindiagnosis;

import dev.jpa.team1.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skin_diagnosis")
@RequiredArgsConstructor
public class SkinDiagnosisController {

    private final SkinDiagnosisService skinDiagnosisService;


    /**
     * 피부 자가진단 결과 저장
     *
     * POST
     * /skin_diagnosis
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SkinDiagnosisResponseDto>>
    saveDiagnosis(
            @RequestBody SkinDiagnosisRequestDto requestDto
    ) {

        SkinDiagnosisResponseDto response =
                skinDiagnosisService
                        .saveDiagnosis(
                                requestDto
                        );


        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(
                        ApiResponse.success(
                                response
                        )
                );
    }


    /**
     * 회원 피부 진단 이력 조회
     *
     * GET
     * /skin_diagnosis/member/{memberId}
     */
    @GetMapping(
            "/member/{memberId}"
    )
    public ResponseEntity<ApiResponse<List<SkinDiagnosisResponseDto>>>
    getMemberDiagnosisHistory(
            @PathVariable("memberId")
            Long memberId
    ) {

        List<SkinDiagnosisResponseDto> response =
                skinDiagnosisService
                        .getMemberDiagnosisHistory(
                                memberId
                        );


        return ResponseEntity.ok(
                ApiResponse.success(
                        response
                )
        );
    }
}