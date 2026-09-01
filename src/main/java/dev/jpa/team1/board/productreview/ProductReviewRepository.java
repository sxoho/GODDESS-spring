package dev.jpa.team1.board.productreview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductReviewRepository
        extends JpaRepository<ProductReview, Long> {

    Page<ProductReview> findByProductIdAndVisible(
            Long productId,
            String visible,
            Pageable pageable
    );

    Page<ProductReview> findByMemberId(
            Long memberId,
            Pageable pageable
    );

    Optional<ProductReview> findByIdAndMemberId(
            Long reviewId,
            Long memberId
    );

    Optional<ProductReview> findByOrderDetailId(Long orderDetailId);

    boolean existsByOrderDetailId(Long orderDetailId);

    long countByProductIdAndVisible(
            Long productId,
            String visible
    );

    @Query("""
            select coalesce(avg(r.rating), 0)
            from ProductReview r
            where r.product.id = :productId
              and r.visible = 'Y'
            """)
    Double findAverageRatingByProductId(
            @Param("productId") Long productId
    );

    @Query("""
            select r
            from ProductReview r
            where (:keyword is null
                   or lower(r.title) like lower(concat('%', :keyword, '%'))
                   or lower(r.product.name) like lower(concat('%', :keyword, '%'))
                   or lower(r.member.nickname) like lower(concat('%', :keyword, '%')))
              and (:visible is null or r.visible = :visible)
              and (:rating is null or r.rating = :rating)
            """)
    Page<ProductReview> findAdminPage(
            @Param("keyword") String keyword,
            @Param("visible") String visible,
            @Param("rating") Integer rating,
            Pageable pageable
    );
}
