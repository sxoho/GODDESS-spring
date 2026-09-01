package dev.jpa.team1.commerce.wishlist;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService service;

    // 찜 추가
    @PostMapping("/save")
    public WishlistResponseDto save(
            @Valid
            @RequestBody
            WishlistRequestDto requestDto
    ) {
        return service.save(requestDto);
    }

    // 기존 회원별 전체 찜 목록
    @GetMapping("/member/{memberId}")
    public List<WishlistResponseDto>
    findByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {
        return service
                .findByMemberId(memberId);
    }

    // 회원별 서버 검색 / 카테고리 필터 / 페이징
    @GetMapping("/member/{memberId}/page")
    public Page<WishlistResponseDto> findPage(
            @PathVariable("memberId")
            Long memberId,

            @RequestParam(
                    name = "keyword",
                    required = false
            )
            String keyword,

            @RequestParam(
                    name = "mainCategoryId",
                    required = false
            )
            Long mainCategoryId,

            @RequestParam(
                    name = "categoryId",
                    required = false
            )
            Long categoryId,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "12"
            )
            int size
    ) {
        return service.findPage(
                memberId,
                keyword,
                mainCategoryId,
                categoryId,
                page,
                size
        );
    }

    // 회원이 실제로 찜한 상품에 존재하는 카테고리만 조회
    @GetMapping("/member/{memberId}/categories")
    public List<WishlistCategoryResponseDto>
    findCategories(
            @PathVariable("memberId")
            Long memberId
    ) {
        return service
                .findCategories(memberId);
    }

    // 찜 여부 확인
    @GetMapping("/check")
    public boolean exists(
            @RequestParam("memberId")
            Long memberId,

            @RequestParam("productId")
            Long productId
    ) {
        return service.exists(
                memberId,
                productId
        );
    }

    // 회원 찜 개수
    @GetMapping("/member/{memberId}/count")
    public long countByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {
        return service
                .countByMemberId(memberId);
    }

    // 상품 찜 개수
    @GetMapping("/product/{productId}/count")
    public long countByProductId(
            @PathVariable("productId")
            Long productId
    ) {
        return service
                .countByProductId(productId);
    }

    // 찜 삭제
    @DeleteMapping(
            "/member/{memberId}/product/{productId}"
    )
    public void delete(
            @PathVariable("memberId")
            Long memberId,

            @PathVariable("productId")
            Long productId
    ) {
        service.delete(
                memberId,
                productId
        );
    }
}
