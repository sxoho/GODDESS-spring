package dev.jpa.team1.fitting.virtualfitting;

import dev.jpa.team1.common.ApiResponse;
import dev.jpa.team1.fitting.coordfitting.CoordFittingRequestDto;
import dev.jpa.team1.fitting.coordfitting.CoordFittingResponseDto;
import dev.jpa.team1.fitting.fittingsave.FittingSaveRequestDto;
import dev.jpa.team1.fitting.fittingsave.FittingSaveResponseDto;
import dev.jpa.team1.fitting.quickfitting.QuickFittingRequestDto;
import dev.jpa.team1.fitting.quickfitting.QuickFittingResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fittings")
@RequiredArgsConstructor
public class VirtualFittingController {

    private final VirtualFittingService virtualFittingService;

    // 1. 간편 피팅 요청 생성 API
    @PostMapping("/quick")
    public ResponseEntity<ApiResponse<QuickFittingResponseDto>> createQuickFitting(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody QuickFittingRequestDto requestDto) {

        QuickFittingResponseDto response = virtualFittingService.createQuickFitting(userDetails.getUsername(), requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 2. [FastAPI 웹훅/콜백 전용] AI 간편 피팅 결과 DB 등록 API
    @PostMapping("/callback/quick/{quickFittingId}")
    public ResponseEntity<ApiResponse<QuickFittingResponseDto>> completeQuickFittingCallback(
            @PathVariable Long quickFittingId,
            @RequestParam Long productColorId,
            @RequestParam String fileOrigin,
            @RequestParam String fileSaved,
            @RequestParam String filePath,
            @RequestParam(required = false) String modelFilePath,
            @RequestParam String aiDescription) {

        QuickFittingResponseDto response = virtualFittingService.completeQuickFittingResult(
                quickFittingId, productColorId, fileOrigin, fileSaved, filePath, modelFilePath, aiDescription);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 3. 코디 피팅 요청 생성 API
    @PostMapping("/coord")
    public ResponseEntity<ApiResponse<CoordFittingResponseDto>> createCoordFitting(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CoordFittingRequestDto requestDto) {

        CoordFittingResponseDto response = virtualFittingService.createCoordFitting(userDetails.getUsername(), requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 4. 피팅 결과 비교함/보관함 저장 API
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<FittingSaveResponseDto>> saveFittingToBox(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody FittingSaveRequestDto requestDto) {

        FittingSaveResponseDto response = virtualFittingService.saveFittingToBox(userDetails.getUsername(), requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 5. 내 피팅 비교함 목록 조회 API
    @GetMapping("/save")
    public ResponseEntity<ApiResponse<List<FittingSaveResponseDto>>> getMySavedFittings(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<FittingSaveResponseDto> response = virtualFittingService.getMySavedFittings(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}