package dev.jpa.team1.board.productqna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * 상품 문의 Repository
 */
public interface ProductQnaRepository
        extends JpaRepository<ProductQna, Long>,
        JpaSpecificationExecutor<ProductQna> {

    List<ProductQna> findByProductIdOrderByCreatedAtDesc(Long productId);

    Page<ProductQna> findByProductIdOrderByCreatedAtDesc(
            Long productId,
            Pageable pageable
    );

    List<ProductQna> findByProductIdAndStatusOrderByCreatedAtDesc(
            Long productId,
            String status
    );

    List<ProductQna> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    // 마이페이지 상품문의 서버 페이징
    Page<ProductQna> findByMemberIdOrderByCreatedAtDesc(
            Long memberId,
            Pageable pageable
    );

    List<ProductQna> findByMemberIdAndStatusOrderByCreatedAtDesc(
            Long memberId,
            String status
    );

    List<ProductQna> findByMemberIdAndProductIdOrderByCreatedAtDesc(
            Long memberId,
            Long productId
    );

    List<ProductQna> findBySecretOrderByCreatedAtDesc(String secret);

    List<ProductQna> findByProductIdAndSecretOrderByCreatedAtDesc(
            Long productId,
            String secret
    );

    List<ProductQna> findByStatusOrderByCreatedAtDesc(String status);

    List<ProductQna> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            String title
    );

    List<ProductQna> findByAnswererIdOrderByAnswerDateDesc(Long answererId);

    long countByProductId(Long productId);

    long countByMemberId(Long memberId);

    long countByStatus(String status);

    boolean existsByIdAndStatus(Long productQnaId, String status);

    Optional<ProductQna> findByIdAndMemberId(
            Long productQnaId,
            Long memberId
    );
}
