package dev.jpa.team1.commerce.orderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long>{
 List<OrderDetail> findByOrdersIdOrderByIdAsc(Long orderId); Optional<OrderDetail> findByOrdersIdAndProductOptionId(Long orderId,Long productOptionId); boolean existsByOrdersIdAndProductOptionId(Long orderId,Long productOptionId); List<OrderDetail> findByProductOptionIdOrderByIdDesc(Long productOptionId); List<OrderDetail> findByProductNameContainingIgnoreCaseOrderByIdDesc(String productName); long countByOrdersId(Long orderId); void deleteByOrdersId(Long orderId);
}
