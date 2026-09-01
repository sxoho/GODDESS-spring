package dev.jpa.team1.board.customerqna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CustomerQnaRepository
        extends JpaRepository<CustomerQna, Long>,
        JpaSpecificationExecutor<CustomerQna> {


    /**
     * 특정 회원 전체 고객 문의
     */
    List<CustomerQna>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );


    /**
     * 마이페이지 회원별 고객 문의 페이징
     */
    Page<CustomerQna>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId,
            Pageable pageable
    );


    /**
     * 회원 + 상태
     */
    List<CustomerQna>
    findByMemberIdAndStatusOrderByCreatedAtDesc(
            Long memberId,
            String status
    );


    /**
     * 회원 + 카테고리
     */
    List<CustomerQna>
    findByMemberIdAndCategoryOrderByCreatedAtDesc(
            Long memberId,
            String category
    );


    /**
     * 특정 주문 문의
     */
    List<CustomerQna>
    findByOrdersIdOrderByCreatedAtDesc(
            Long orderId
    );


    /**
     * 회원 + 주문
     */
    List<CustomerQna>
    findByMemberIdAndOrdersIdOrderByCreatedAtDesc(
            Long memberId,
            Long orderId
    );


    /**
     * 상태별 전체 문의
     */
    List<CustomerQna>
    findByStatusOrderByCreatedAtDesc(
            String status
    );


    /**
     * 문의유형별 전체 문의
     */
    List<CustomerQna>
    findByCategoryOrderByCreatedAtDesc(
            String category
    );


    /**
     * 본인 문의 확인
     */
    Optional<CustomerQna>
    findByIdAndMemberId(
            Long customerQnaId,
            Long memberId
    );


    /**
     * 회원 문의 개수
     */
    long countByMemberId(
            Long memberId
    );


    /**
     * 상태별 문의 개수
     */
    long countByStatus(
            String status
    );
}