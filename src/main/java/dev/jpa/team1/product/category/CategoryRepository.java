package dev.jpa.team1.product.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 카테고리 Repository
 *
 * CATEGORY 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * Repository는 브라우저에서 직접 실행하는 URL이 없다.
 * 추후 Controller 연결 예시:
 * GET http://localhost:포트번호/categories
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 카테고리 이름으로 카테고리 한 건 조회
     *
     * 카테고리명 중복 검사나 특정 카테고리 검색에 사용할 수 있다.
     */
    Optional<Category> findByName(String name);

    /**
     * 동일한 카테고리 이름이 존재하는지 확인
     *
     * true  : 같은 이름의 카테고리가 존재함
     * false : 같은 이름의 카테고리가 존재하지 않음
     */
    boolean existsByName(String name);

    /**
     * 최상위 카테고리 목록 조회
     *
     * parent가 null인 카테고리가 최상위 카테고리이다.
     * sortOrder 오름차순으로 정렬한다.
     */
    List<Category> findByParentIsNullOrderBySortOrderAsc();

    /**
     * 공개 중인 최상위 카테고리 목록 조회
     *
     * 예:
     * visible = "Y"
     */
    List<Category> findByParentIsNullAndVisibleOrderBySortOrderAsc(
            String visible
    );

    /**
     * 특정 상위 카테고리에 속한 하위 카테고리 목록 조회
     *
     * parentId는 상위 카테고리의 PK이다.
     */
    List<Category> findByParentIdOrderBySortOrderAsc(Long parentId);

    /**
     * 특정 상위 카테고리에 속한 공개 카테고리 목록 조회
     */
    List<Category> findByParentIdAndVisibleOrderBySortOrderAsc(
            Long parentId,
            String visible
    );

    /**
     * 공개 여부에 따라 전체 카테고리 조회
     *
     * sortOrder 오름차순으로 정렬한다.
     */
    List<Category> findByVisibleOrderBySortOrderAsc(String visible);
}