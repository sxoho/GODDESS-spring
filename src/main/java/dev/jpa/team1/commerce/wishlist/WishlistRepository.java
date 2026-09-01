package dev.jpa.team1.commerce.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Long>,
        JpaSpecificationExecutor<Wishlist> {

    List<Wishlist>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );

    Optional<Wishlist>
    findByMemberIdAndProductId(
            Long memberId,
            Long productId
    );

    boolean existsByMemberIdAndProductId(
            Long memberId,
            Long productId
    );

    List<Wishlist>
    findByProductIdOrderByCreatedAtDesc(
            Long productId
    );

    long countByMemberId(
            Long memberId
    );

    long countByProductId(
            Long productId
    );

    void deleteByMemberIdAndProductId(
            Long memberId,
            Long productId
    );

    void deleteByMemberId(
            Long memberId
    );

    /**
     * 해당 회원이 실제로 찜한 상품에 존재하는 카테고리만 조회
     */
    @Query(
            value = """
                    SELECT
                        c.category_id AS categoryId,
                        c.name AS categoryName,
                        c.parent_id AS parentId,
                        parent_c.name AS parentName,
                        COUNT(*) AS productCount
                    FROM wishlist w
                    JOIN product p
                      ON p.product_id = w.product_id
                    JOIN category c
                      ON c.category_id = p.category_id
                    LEFT JOIN category parent_c
                      ON parent_c.category_id = c.parent_id
                    WHERE w.member_id = :memberId
                    GROUP BY
                        c.category_id,
                        c.name,
                        c.parent_id,
                        parent_c.name,
                        c.sort_order,
                        parent_c.sort_order
                    ORDER BY
                        NVL(parent_c.sort_order, c.sort_order),
                        c.sort_order,
                        c.category_id
                    """,
            nativeQuery = true
    )
    List<WishlistCategoryProjection>
    findWishlistCategories(
            @Param("memberId")
            Long memberId
    );
}
