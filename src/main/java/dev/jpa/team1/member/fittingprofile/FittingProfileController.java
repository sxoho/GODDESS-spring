package dev.jpa.team1.member.fittingprofile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * 피팅 프로필 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/fitting_profile")
public class FittingProfileController {

    private final FittingProfileService
        fittingProfileService;


    /**
     * 회원의 피팅 프로필 존재 여부
     *
     * GET
     * /fitting_profile/member/{memberId}/exists
     */
    @GetMapping(
        "/member/{memberId}/exists"
    )
    public ResponseEntity<Boolean> exists(
        @PathVariable("memberId")
        Long memberId
    ) {

        return ResponseEntity.ok(
            fittingProfileService
                .existsByMemberId(
                    memberId
                )
        );
    }


    /**
     * 회원 피팅 프로필 조회
     *
     * GET
     * /fitting_profile/member/{memberId}
     */
    @GetMapping(
        "/member/{memberId}"
    )
    public ResponseEntity<FittingProfileResponseDto>
        findByMemberId(
            @PathVariable("memberId")
            Long memberId
        ) {

        return ResponseEntity.ok(
            fittingProfileService
                .findByMemberId(
                    memberId
                )
        );
    }


    /**
     * 피팅 프로필 최초 등록
     *
     * POST
     * /fitting_profile
     */
    @PostMapping
    public ResponseEntity<FittingProfileResponseDto>
        save(
            @Valid
            @RequestBody
            FittingProfileRequestDto request
        ) {

        return ResponseEntity.ok(
            fittingProfileService
                .save(
                    request
                )
        );
    }


    /**
     * 회원 피팅 프로필 수정
     *
     * PUT
     * /fitting_profile/member/{memberId}
     */
    @PutMapping(
        "/member/{memberId}"
    )
    public ResponseEntity<FittingProfileResponseDto>
        update(
            @PathVariable("memberId")
            Long memberId,

            @Valid
            @RequestBody
            FittingProfileRequestDto request
        ) {

        return ResponseEntity.ok(
            fittingProfileService
                .update(
                    memberId,
                    request
                )
        );
    }
}