package dev.jpa.team1.commerce.orderdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 주문 상세 Repository
 *
 * ORDER_DETAIL 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 주문에 포함된 상품 옵션, 상품명, 색상명,
 * 사이즈명, 가격, 수량 정보를 관리한다.
 *
 * 주문 시점의 상품 정보를 보존하기 위해
 * 상품명, 색상명, 사이즈명, 가격을 별도로 저장한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/api/orders/{orderId}/details
 * GET http://localhost:포트번호/api/order-details/{orderDetailId}
 */
public interface OrderDetailRepository
        extends JpaRepository<OrderDetail, Long> {

    /**
     * 특정 주문에 포함된 전체 주문 상세 조회
     *
     * PK 오름차순으로 조회한다.
     */
    List<OrderDetail> findByOrdersIdOrderByIdAsc(Long orderId);

    /**
     * 특정 주문과 상품 옵션에 해당하는
     * 주문 상세 한 건 조회
     */
    Optional<OrderDetail> findByOrdersIdAndProductOptionId(
            Long orderId,
            Long productOptionId
    );

    /**
     * 특정 주문에 동일한 상품 옵션이 존재하는지 확인
     */
    boolean existsByOrdersIdAndProductOptionId(
            Long orderId,
            Long productOptionId
    );

    /**
     * 특정 상품 옵션이 포함된 주문 상세 목록 조회
     */
    List<OrderDetail> findByProductOptionIdOrderByIdDesc(
            Long productOptionId
    );

    /**
     * 특정 간편 피팅 결과를 통해 주문된 상세 목록 조회
     */
    List<OrderDetail> findByQuickFittingResultIdOrderByIdDesc(
            Long quickFittingResultId
    );

    /**
     * 특정 코디 피팅 결과를 통해 주문된 상세 목록 조회
     */
    List<OrderDetail> findByCoordFittingResultIdOrderByIdDesc(
            Long coordFittingResultId
    );

    /**
     * 상품명에 검색어가 포함된 주문 상세 조회
     *
     * 주문 당시 저장된 상품명을 기준으로 검색한다.
     */
    List<OrderDetail>
            findByProductNameContainingIgnoreCaseOrderByIdDesc(
                    String productName
            );

    /**
     * 특정 주문의 주문 상세 개수 조회
     */
    long countByOrdersId(Long orderId);

    /**
     * 특정 주문의 전체 주문 상세 삭제
     */
    void deleteByOrdersId(Long orderId);
}