package dev.jpa.team1.commerce.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 Repository
 *
 * CART 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 장바구니에는 상품 옵션, 수량과 함께
 * 간편 피팅 결과 또는 코디 피팅 결과가 연결될 수 있다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST   http://localhost:포트번호/api/carts
 * GET    http://localhost:포트번호/api/members/{memberId}/carts
 * PATCH  http://localhost:포트번호/api/carts/{cartId}
 * DELETE http://localhost:포트번호/api/carts/{cartId}
 */
public interface CartRepository
        extends JpaRepository<Cart, Long> {

    /**
     * 특정 회원의 전체 장바구니 목록 조회
     *
     * 최근에 담은 상품부터 조회한다.
     */
    List<Cart> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /**
     * 특정 회원의 특정 상품 옵션 장바구니 조회
     *
     * 같은 옵션이 이미 담겨 있는지 확인할 때 사용한다.
     */
    Optional<Cart> findByMemberIdAndProductOptionId(
            Long memberId,
            Long productOptionId
    );

    /**
     * 특정 회원의 장바구니에 같은 상품 옵션이
     * 이미 존재하는지 확인
     */
    boolean existsByMemberIdAndProductOptionId(
            Long memberId,
            Long productOptionId
    );

    /**
     * 특정 상품 옵션이 담긴 전체 장바구니 기록 조회
     */
    List<Cart> findByProductOptionIdOrderByCreatedAtDesc(
            Long productOptionId
    );

    /**
     * 특정 간편 피팅 결과와 연결된 장바구니 조회
     */
    List<Cart> findByQuickFittingResultIdOrderByCreatedAtDesc(
            Long quickFittingResultId
    );

    /**
     * 특정 코디 피팅 결과와 연결된 장바구니 조회
     */
    List<Cart> findByCoordFittingResultIdOrderByCreatedAtDesc(
            Long coordFittingResultId
    );

    /**
     * 특정 회원의 장바구니 상품 종류 개수 조회
     *
     * 장바구니 행 개수를 반환한다.
     */
    long countByMemberId(Long memberId);

    /**
     * 특정 회원의 특정 상품 옵션 장바구니 삭제
     */
    void deleteByMemberIdAndProductOptionId(
            Long memberId,
            Long productOptionId
    );

    /**
     * 특정 회원의 장바구니 전체 삭제
     *
     * 주문 완료 후 장바구니 비우기 등에 사용할 수 있다.
     */
    void deleteByMemberId(Long memberId);
}