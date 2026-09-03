package dev.jpa.team1.commerce.cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CartRepository extends JpaRepository<Cart,Long>{
 List<Cart> findByMemberIdOrderByCreatedAtDesc(Long memberId);
 Optional<Cart> findByMemberIdAndProductOptionId(Long memberId,Long productOptionId);
 boolean existsByMemberIdAndProductOptionId(Long memberId,Long productOptionId);
 long countByMemberId(Long memberId);
 void deleteByMemberIdAndProductOptionId(Long memberId,Long productOptionId);
 void deleteByMemberId(Long memberId);
}
