package dev.jpa.team1.beauty.recommendation;

import dev.jpa.team1.product.productcolor.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 뷰티 추천 후보 상품 조회 전용 Repository.
 *
 * 기존 ProductColorRepository / ProductColorPersonalColorRepository를 수정하지 않고
 * 추천 기능에서 필요한 조회만 독립적으로 담당한다.
 */
public interface BeautyRecommendationQueryRepository
        extends JpaRepository<ProductColor, Long> {

    /**
     * 특정 퍼스널컬러에 매핑된 판매중/노출중 상품 색상을 조회한다.
     */
    @Query("""
            select pc
            from ProductColorPersonalColor mapping
            join mapping.productColor pc
            join fetch pc.product p
            join fetch pc.colorMaster cm
            where mapping.personalColorType = :personalColor
              and pc.visible = 'Y'
              and p.status = 'ON_SALE'
            order by p.viewCount desc, pc.sortOrder asc, p.id desc
            """)
    List<ProductColor> findByPersonalColor(
            @Param("personalColor") String personalColor
    );

    /**
     * 퍼스널컬러 정보가 없을 때 사용할 전체 판매중/노출중 후보.
     */
    @Query("""
            select pc
            from ProductColor pc
            join fetch pc.product p
            join fetch pc.colorMaster cm
            where pc.visible = 'Y'
              and p.status = 'ON_SALE'
            order by p.viewCount desc, pc.sortOrder asc, p.id desc
            """)
    List<ProductColor> findAllAvailable();
}
