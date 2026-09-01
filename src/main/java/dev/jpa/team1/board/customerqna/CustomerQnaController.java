package dev.jpa.team1.board.customerqna;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer_qna")
@RequiredArgsConstructor
public class CustomerQnaController {

    private final CustomerQnaService service;


    /**
     * 고객 문의 등록
     */
    @PostMapping
    public CustomerQnaResponseDto save(
            @Valid
            @RequestBody
            CustomerQnaRequestDto requestDto
    ) {

        return service.save(
                requestDto
        );
    }


    /**
     * 회원별 고객 문의 전체 조회
     */
    @GetMapping("/member/{memberId}")
    public List<CustomerQnaResponseDto>
    findByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {

        return service.findByMemberId(
                memberId
        );
    }


    /**
     * 마이페이지 고객 문의 페이징
     *
     * 예:
     * /customer_qna/member/8/page?page=0&size=5
     */
    @GetMapping(
            "/member/{memberId}/page"
    )
    public Page<CustomerQnaResponseDto>
    findMemberPage(

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


    /**
     * 회원 본인 문의 상세
     */
    @GetMapping(
            "/{customerQnaId}/member/{memberId}"
    )
    public CustomerQnaResponseDto
    findMemberDetail(

            @PathVariable("customerQnaId")
            Long customerQnaId,

            @PathVariable("memberId")
            Long memberId
    ) {

        return service.findMemberDetail(
                customerQnaId,
                memberId
        );
    }


    /**
     * 고객 문의 수정
     */
    @PutMapping("/{customerQnaId}")
    public CustomerQnaResponseDto update(

            @PathVariable("customerQnaId")
            Long customerQnaId,

            @Valid
            @RequestBody
            CustomerQnaUpdateRequestDto requestDto
    ) {

        return service.update(
                customerQnaId,
                requestDto
        );
    }


    /**
     * 고객 문의 삭제
     */
    @DeleteMapping(
            "/{customerQnaId}/member/{memberId}"
    )
    public void delete(

            @PathVariable("customerQnaId")
            Long customerQnaId,

            @PathVariable("memberId")
            Long memberId
    ) {

        service.delete(
                customerQnaId,
                memberId
        );
    }


    /**
     * 관리자 고객문의
     *
     * 전체 / 답변대기 / 답변완료
     * 검색
     * 페이징
     *
     * 예:
     *
     * /customer_qna/admin/page
     *
     * /customer_qna/admin/page
     * ?status=WAITING
     *
     * /customer_qna/admin/page
     * ?status=ANSWERED
     *
     * /customer_qna/admin/page
     * ?keyword=배송
     */
    @GetMapping("/admin/page")
    public Page<CustomerQnaResponseDto>
    findAdminPage(

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
     * 관리자 문의 상세
     */
    @GetMapping(
            "/admin/{customerQnaId}"
    )
    public CustomerQnaResponseDto
    findAdminDetail(

            @PathVariable("customerQnaId")
            Long customerQnaId
    ) {

        return service.findAdminDetail(
                customerQnaId
        );
    }


    /**
     * 관리자 답변 등록 / 수정
     *
     * 등록 시
     * WAITING -> ANSWERED
     */
    @PutMapping(
            "/admin/{customerQnaId}/answer"
    )
    public CustomerQnaResponseDto answer(

            @PathVariable("customerQnaId")
            Long customerQnaId,

            @Valid
            @RequestBody
            CustomerQnaAnswerRequestDto requestDto
    ) {

        return service.answer(
                customerQnaId,
                requestDto
        );
    }
    
    /**
     * 회원별 고객 문의 AI 요약
     *
     * 예:
     * /customer_qna/member/4/ai_summary
     */
    @GetMapping(
            "/member/{memberId}/ai_summary"
    )
    public CustomerQnaAiSummaryDto
    getAiSummary(

            @PathVariable("memberId")
            Long memberId
    ) {

        return service.getAiSummary(
                memberId
        );
    }


    /**
     * 회원 고객문의 개수
     */
    @GetMapping(
            "/member/{memberId}/count"
    )
    public long countByMemberId(

            @PathVariable("memberId")
            Long memberId
    ) {

        return service.countByMemberId(
                memberId
        );
    }


    /**
     * 관리자 대시보드 등에서
     * 답변대기 개수 조회 가능
     */
    @GetMapping(
            "/status/{status}/count"
    )
    public long countByStatus(

            @PathVariable("status")
            String status
    ) {

        return service.countByStatus(
                status
        );
    }
}