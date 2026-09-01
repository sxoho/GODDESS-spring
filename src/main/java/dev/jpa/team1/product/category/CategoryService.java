package dev.jpa.team1.product.category;

import dev.jpa.team1.product.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final ProductRepository productRepository;

    /**
     * 쇼핑몰용 전체 카테고리 조회
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return repository.findAll().stream()
                .map(CategoryResponseDto::from)
                .toList();
    }

    /**
     * 관리자 카테고리 관리용 계층형 조회
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findTree() {
        return repository.findByParentIsNullOrderBySortOrderAsc().stream()
                .map(CategoryResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto findById(Long id) {
        Category entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found. id=" + id
                        )
                );

        return CategoryResponseDto.from(entity);
    }

    /**
     * 카테고리 등록
     *
     * 입력한 정렬 순서 위치에 새 카테고리를 넣고
     * 뒤의 카테고리는 자동으로 한 칸씩 밀린다.
     */
    @Transactional
    public CategoryResponseDto save(CategoryRequestDto requestDto) {
        Category parent = getParent(requestDto.getParentId());

        List<Category> siblings =
                new ArrayList<>(
                        getSiblings(
                                requestDto.getParentId()
                        )
                );

        int newPosition =
                normalizePosition(
                        requestDto.getSortOrder(),
                        siblings.size() + 1
                );

        Category entity = Category.builder()
                .parent(parent)
                .name(requestDto.getName().trim())
                .description(requestDto.getDescription())
                .sortOrder(newPosition)
                .visible(requestDto.getVisible())
                .build();

        // 먼저 저장해서 ID를 생성한다.
        repository.save(entity);

        // 원하는 위치에 넣고 전체 순서를 1부터 다시 맞춘다.
        siblings.add(
                newPosition - 1,
                entity
        );

        reorder(siblings);

        return CategoryResponseDto.from(entity);
    }

    /**
     * 카테고리 수정
     *
     * 예)
     * 1, 2, 3, 4 에서
     * 3번을 4번으로 이동하면
     * 기존 4번이 자동으로 3번이 된다.
     */
    @Transactional
    public CategoryResponseDto update(
            Long id,
            CategoryRequestDto requestDto
    ) {
        Category entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found. id=" + id
                        )
                );

        Category parent =
                getParent(
                        requestDto.getParentId()
                );

        if (
                parent != null &&
                parent.getId().equals(id)
        ) {
            throw new IllegalArgumentException(
                    "자기 자신을 상위 카테고리로 지정할 수 없습니다."
            );
        }

        Long oldParentId =
                entity.getParent() == null
                        ? null
                        : entity.getParent().getId();

        Long newParentId =
                parent == null
                        ? null
                        : parent.getId();

        boolean sameParent =
                Objects.equals(
                        oldParentId,
                        newParentId
                );

        if (sameParent) {
            // 같은 대분류 안에서 순서만 변경
            List<Category> siblings =
                    new ArrayList<>(
                            getSiblings(
                                    oldParentId
                            )
                    );

            siblings.removeIf(
                    category ->
                            category.getId()
                                    .equals(id)
            );

            int newPosition =
                    normalizePosition(
                            requestDto.getSortOrder(),
                            siblings.size() + 1
                    );

            // 수정 대상의 정보 먼저 변경
            entity.update(
                    parent,
                    requestDto.getName().trim(),
                    requestDto.getDescription(),
                    newPosition,
                    requestDto.getVisible()
            );

            // 원하는 위치에 다시 넣는다.
            siblings.add(
                    newPosition - 1,
                    entity
            );

            // 1, 2, 3 ... 순서로 자동 정리
            reorder(siblings);

        } else {
            // 상위 카테고리까지 변경되는 경우

            // 기존 그룹에서 제거 후 순서 정리
            List<Category> oldSiblings =
                    new ArrayList<>(
                            getSiblings(
                                    oldParentId
                            )
                    );

            oldSiblings.removeIf(
                    category ->
                            category.getId()
                                    .equals(id)
            );

            reorder(oldSiblings);

            // 새 그룹 조회
            List<Category> newSiblings =
                    new ArrayList<>(
                            getSiblings(
                                    newParentId
                            )
                    );

            newSiblings.removeIf(
                    category ->
                            category.getId()
                                    .equals(id)
            );

            int newPosition =
                    normalizePosition(
                            requestDto.getSortOrder(),
                            newSiblings.size() + 1
                    );

            // 수정 대상 정보 변경
            entity.update(
                    parent,
                    requestDto.getName().trim(),
                    requestDto.getDescription(),
                    newPosition,
                    requestDto.getVisible()
            );

            // 새 그룹의 원하는 위치에 추가
            newSiblings.add(
                    newPosition - 1,
                    entity
            );

            reorder(newSiblings);
        }

        return CategoryResponseDto.from(entity);
    }

    /**
     * 카테고리 삭제
     *
     * 삭제 후 남은 카테고리의 정렬 순서도
     * 1부터 자동으로 다시 맞춘다.
     */
    @Transactional
    public void delete(Long id) {
        Category entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found. id=" + id
                        )
                );

        if (!entity.getChildren().isEmpty()) {
            throw new IllegalArgumentException(
                    "하위 카테고리가 있어 삭제할 수 없습니다."
            );
        }

        if (productRepository.existsByCategory_Id(id)) {
            throw new IllegalArgumentException(
                    "등록된 상품이 있는 카테고리는 삭제할 수 없습니다."
            );
        }

        Long parentId =
                entity.getParent() == null
                        ? null
                        : entity.getParent().getId();

        List<Category> siblings =
                new ArrayList<>(
                        getSiblings(parentId)
                );

        siblings.removeIf(
                category ->
                        category.getId()
                                .equals(id)
        );

        repository.delete(entity);

        // 삭제 후 1, 2, 3 ... 다시 정리
        reorder(siblings);
    }

    /**
     * 같은 상위 카테고리에 속한 카테고리 조회
     */
    private List<Category> getSiblings(
            Long parentId
    ) {
        if (parentId == null) {
            return repository
                    .findByParentIsNullOrderBySortOrderAsc();
        }

        return repository
                .findByParentIdOrderBySortOrderAsc(
                        parentId
                );
    }

    /**
     * 정렬 순서를 1부터 연속된 숫자로 다시 맞춘다.
     */
    private void reorder(
            List<Category> categories
    ) {
        for (
                int index = 0;
                index < categories.size();
                index++
        ) {
            Category category =
                    categories.get(index);

            int sortOrder =
                    index + 1;

            if (
                    category.getSortOrder() == null ||
                    category.getSortOrder() != sortOrder
            ) {
                category.update(
                        category.getParent(),
                        category.getName(),
                        category.getDescription(),
                        sortOrder,
                        category.getVisible()
                );
            }
        }
    }

    /**
     * 0이나 너무 큰 숫자를 입력해도
     * 가능한 범위 안으로 자동 보정한다.
     */
    private int normalizePosition(
            Integer requestedPosition,
            int maxPosition
    ) {
        if (
                requestedPosition == null ||
                requestedPosition < 1
        ) {
            return 1;
        }

        return Math.min(
                requestedPosition,
                maxPosition
        );
    }

    private Category getParent(
            Long parentId
    ) {
        if (parentId == null) {
            return null;
        }

        Category parent = repository.findById(parentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Parent category not found. id="
                                        + parentId
                        )
                );

        if (parent.getParent() != null) {
            throw new IllegalArgumentException(
                    "하위 카테고리 아래에는 카테고리를 추가할 수 없습니다."
            );
        }

        return parent;
    }
}
