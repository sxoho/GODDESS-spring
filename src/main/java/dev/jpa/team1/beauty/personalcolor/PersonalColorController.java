package dev.jpa.team1.beauty.personalcolor;

import dev.jpa.team1.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personal_color")
@RequiredArgsConstructor
public class PersonalColorController {

    private final PersonalColorService personalColorService;


    /**
     * 퍼스널컬러 자가진단 결과 저장
     *
     * POST
     * /personal_color
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PersonalColorResponseDto>>
    saveDiagnosis(
            @RequestBody PersonalColorRequestDto requestDto
    ) {

        PersonalColorResponseDto response =
                personalColorService
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
     * 회원 퍼스널컬러 진단 이력 조회
     *
     * GET
     * /personal_color/member/{memberId}
     */
    @GetMapping(
            "/member/{memberId}"
    )
    public ResponseEntity<ApiResponse<List<PersonalColorResponseDto>>>
    getMemberDiagnosisHistory(
            @PathVariable("memberId")
            Long memberId
    ) {

        List<PersonalColorResponseDto> response =
                personalColorService
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