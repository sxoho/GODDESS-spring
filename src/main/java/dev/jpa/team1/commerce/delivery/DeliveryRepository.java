package dev.jpa.team1.commerce.delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 배송 Repository
 *
 * DELIVERY 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 주문별 수령인, 연락처, 주소, 배송 요청사항,
 * 택배사, 운송장 번호와 배송 상태를 관리한다.
 *
 * Orders와 Delivery는 1:1 관계이다.
 * 따라서 하나의 주문에는 하나의 배송 정보가 존재한다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST  http://localhost:포트번호/api/deliveries
 * GET   http://localhost:포트번호/api/orders/{orderId}/delivery
 * PATCH http://localhost:포트번호/api/deliveries/{deliveryId}/status
 */
public interface DeliveryRepository
        extends JpaRepository<Delivery, Long> {

    /**
     * 특정 주문의 배송 정보 조회
     */
    Optional<Delivery> findByOrdersId(Long orderId);

    /**
     * 특정 주문에 배송 정보가 존재하는지 확인
     */
    boolean existsByOrdersId(Long orderId);

    /**
     * 배송 상태별 배송 목록 조회
     *
     * status 예시:
     * READY, SHIPPING, DELIVERED, RETURNED
     */
    List<Delivery> findByStatusOrderByIdDesc(String status);

    /**
     * 택배사별 배송 목록 조회
     */
    List<Delivery> findByCompanyOrderByIdDesc(String company);

    /**
     * 운송장 번호로 배송 정보 조회
     */
    Optional<Delivery> findByTrackingNo(String trackingNo);

    /**
     * 동일한 운송장 번호가 존재하는지 확인
     */
    boolean existsByTrackingNo(String trackingNo);

    /**
     * 수령인 이름으로 배송 목록 조회
     *
     * 대소문자를 구분하지 않는다.
     */
    List<Delivery>
            findByRecipientContainingIgnoreCaseOrderByIdDesc(
                    String recipient
            );

    /**
     * 연락처로 배송 정보 조회
     */
    List<Delivery> findByTelOrderByIdDesc(String tel);

    /**
     * 특정 주문의 배송 정보 삭제
     */
    void deleteByOrdersId(Long orderId);
}