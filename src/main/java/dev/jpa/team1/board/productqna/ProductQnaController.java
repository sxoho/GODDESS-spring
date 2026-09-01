package dev.jpa.team1.board.productqna;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_qna")
@RequiredArgsConstructor
public class ProductQnaController {

    private final ProductQnaService service;

    /**
     * 문의 등록
     */
    @PostMapping("/save")
    public ProductQnaResponseDto save(
            @Valid
            @RequestBody
            ProductQnaRequestDto requestDto
    ) {
        return service.save(requestDto);
    }

    /**
     * 기존 전체 문의 조회
     */
    @GetMapping("/find_all")
    public List<ProductQnaResponseDto> findAll() {
        return service.findAll();
    }

    /**
     * 관리자 상품문의 서버 페이징 / 검색 / 상태 필터
     */
    @GetMapping("/admin/page")
    public Page<ProductQnaResponseDto> findAdminPage(
            @RequestParam(
                    name = "keyword",
                    required = false
            )
            String keyword,

            @RequestParam(
                    name = "status",
                    required = false
            )
            String status,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "10"
            )
            int size
    ) {
        return service.findAdminPage(
                keyword,
                status,
                page,
                size
        );
    }

    /**
     * 문의 단건 조회
     */
    @GetMapping("/{productQnaId}")
    public ProductQnaResponseDto findById(
            @PathVariable("productQnaId")
            Long productQnaId
    ) {
        return service.findById(productQnaId);
    }

    /**
     * 기존 상품별 전체 문의 조회
     */
    @GetMapping("/product/{productId}")
    public List<ProductQnaPublicResponseDto> findByProductId(
            @PathVariable("productId")
            Long productId,

            @RequestParam(
                    name = "memberId",
                    required = false
            )
            Long memberId
    ) {
        return service.findByProductId(
                productId,
                memberId
        );
    }

    /**
     * 사용자 상품 상세 상품문의 서버 페이징
     */
    @GetMapping("/product/{productId}/page")
    public Page<ProductQnaPublicResponseDto> findProductPage(
            @PathVariable("productId")
            Long productId,

            @RequestParam(
                    name = "memberId",
                    required = false
            )
            Long memberId,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "10"
            )
            int size
    ) {
        return service.findProductPage(
                productId,
                memberId,
                page,
                size
        );
    }

    @GetMapping("/product/{productId}/status/{status}")
    public List<ProductQnaResponseDto> findByProductIdAndStatus(
            @PathVariable("productId")
            Long productId,

            @PathVariable("status")
            String status
    ) {
        return service.findByProductIdAndStatus(
                productId,
                status
        );
    }

    /**
     * 기존 회원별 전체 문의 조회
     */
    @GetMapping("/member/{memberId}")
    public List<ProductQnaResponseDto> findByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {
        return service.findByMemberId(memberId);
    }

    /**
     * 마이페이지 회원별 상품문의 서버 페이징
     *
     * 예:
     * /product_qna/member/8/page?page=0&size=5
     */
    @GetMapping("/member/{memberId}/page")
    public Page<ProductQnaResponseDto> findMemberPage(
            @PathVariable("memberId")
            Long memberId,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "5"
            )
            int size
    ) {
        return service.findMemberPage(
                memberId,
                page,
                size
        );
    }

    @PutMapping("/{productQnaId}")
    public ProductQnaResponseDto update(
            @PathVariable("productQnaId")
            Long productQnaId,

            @Valid
            @RequestBody
            ProductQnaUpdateRequestDto requestDto
    ) {
        return service.update(
                productQnaId,
                requestDto
        );
    }

    @DeleteMapping("/{productQnaId}/member/{memberId}")
    public void delete(
            @PathVariable("productQnaId")
            Long productQnaId,

            @PathVariable("memberId")
            Long memberId
    ) {
        service.delete(
                productQnaId,
                memberId
        );
    }

    @PutMapping("/{productQnaId}/answer")
    public ProductQnaResponseDto answer(
            @PathVariable("productQnaId")
            Long productQnaId,

            @Valid
            @RequestBody
            ProductQnaAnswerRequestDto requestDto
    ) {
        return service.answer(
                productQnaId,
                requestDto
        );
    }

    @PutMapping("/{productQnaId}/close")
    public ProductQnaResponseDto close(
            @PathVariable("productQnaId")
            Long productQnaId
    ) {
        return service.close(productQnaId);
    }

    @GetMapping("/product/{productId}/count")
    public long countByProductId(
            @PathVariable("productId")
            Long productId
    ) {
        return service.countByProductId(productId);
    }

    @GetMapping("/member/{memberId}/count")
    public long countByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {
        return service.countByMemberId(memberId);
    }

    /**
     * 관리자 대시보드 답변 대기 개수 조회에도 사용 가능
     */
    @GetMapping("/status/{status}/count")
    public long countByStatus(
            @PathVariable("status")
            String status
    ) {
        return service.countByStatus(status);
    }
}
