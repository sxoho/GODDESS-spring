package dev.jpa.team1.board.productreview;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product_review")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService service;

    // 리뷰 등록
    @PostMapping("/save")
    public ProductReviewResponseDto save(
            @Valid @RequestBody ProductReviewRequestDto requestDto
    ) {
        return service.save(requestDto);
    }

    // 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ProductReviewResponseDto findById(
            @PathVariable(name = "reviewId") Long reviewId
    ) {
        return service.findById(reviewId);
    }

    // 주문상품 리뷰 작성 여부 확인
    @GetMapping("/order-detail/{orderDetailId}/exists")
    public boolean existsByOrderDetailId(
            @PathVariable(name = "orderDetailId") Long orderDetailId
    ) {
        return service.existsByOrderDetailId(orderDetailId);
    }

    // 상품 상세용 공개 리뷰 페이징
    @GetMapping("/product/{productId}/page")
    public Page<ProductReviewPublicResponseDto> findProductPage(
            @PathVariable(name = "productId") Long productId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return service.findProductPage(
                productId,
                page,
                size
        );
    }

    // 마이페이지 회원별 리뷰 페이징
    @GetMapping("/member/{memberId}/page")
    public Page<ProductReviewResponseDto> findMemberPage(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return service.findMemberPage(
                memberId,
                page,
                size
        );
    }

    // 관리자 전체 리뷰 페이징 + 검색 + 공개여부 + 별점 필터
    @GetMapping("/admin/page")
    public Page<ProductReviewResponseDto> findAdminPage(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "visible", required = false) String visible,
            @RequestParam(name = "rating", required = false) Integer rating,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return service.findAdminPage(
                keyword,
                visible,
                rating,
                page,
                size
        );
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ProductReviewResponseDto update(
            @PathVariable(name = "reviewId") Long reviewId,
            @Valid @RequestBody ProductReviewUpdateRequestDto requestDto
    ) {
        return service.update(
                reviewId,
                requestDto
        );
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}/member/{memberId}")
    public void delete(
            @PathVariable(name = "reviewId") Long reviewId,
            @PathVariable(name = "memberId") Long memberId
    ) {
        service.delete(
                reviewId,
                memberId
        );
    }

    // 관리자 공개/숨김 처리
    @PutMapping("/{reviewId}/visible")
    public ProductReviewResponseDto changeVisible(
            @PathVariable(name = "reviewId") Long reviewId,
            @Valid @RequestBody ProductReviewVisibleRequestDto requestDto
    ) {
        return service.changeVisible(
                reviewId,
                requestDto
        );
    }

    // 상품별 AI 리뷰 요약
    @GetMapping("/product/{productId}/ai-summary")
    public ProductReviewAiSummaryDto getAiSummary(
            @PathVariable(name = "productId") Long productId
    ) {
        return service.getAiSummary(productId);
    }

    // 상품별 리뷰 개수 + 평균 별점
    @GetMapping("/product/{productId}/summary")
    public ProductReviewSummaryDto getSummary(
            @PathVariable(name = "productId") Long productId
    ) {
        return service.getSummary(productId);
    }
}