package dev.jpa.team1.commerce.wishlist;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import dev.jpa.team1.product.category.Category;
import dev.jpa.team1.product.search.ProductSearchKeyword;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository repository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 회원별 전체 찜 목록
    @Transactional(readOnly = true)
    public List<WishlistResponseDto> findByMemberId(
            Long memberId
    ) {
        return repository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(WishlistResponseDto::from)
                .toList();
    }

    // 서버 검색 + 카테고리 필터 + 페이징
    @Transactional(readOnly = true)
    public Page<WishlistResponseDto> findPage(
            Long memberId,
            String keyword,
            Long mainCategoryId,
            Long categoryId,
            int page,
            int size
    ) {

        int safePage =
                Math.max(
                        page,
                        0
                );

        int safeSize =
                Math.min(
                        Math.max(
                                size,
                                1
                        ),
                        100
                );

        Pageable pageable =
                PageRequest.of(
                        safePage,
                        safeSize
                );

        List<String> searchTerms =
                ProductSearchKeyword
                        .buildSearchTerms(
                                keyword
                        );

        Specification<Wishlist> specification =
                buildSearchSpecification(
                        memberId,
                        mainCategoryId,
                        categoryId,
                        searchTerms
                );

        return repository
                .findAll(
                        specification,
                        pageable
                )
                .map(
                        WishlistResponseDto::from
                );
    }

    /**
     * 위시리스트 검색용 Specification
     *
     * 검색 대상:
     * - 상품명
     * - 브랜드
     * - 하위 카테고리명
     * - 대분류명
     *
     * ProductSearchKeyword의 동의어 사전을
     * Product 검색과 동일하게 사용한다.
     */
    private Specification<Wishlist>
    buildSearchSpecification(
            Long memberId,
            Long mainCategoryId,
            Long categoryId,
            List<String> searchTerms
    ) {

        return (
                root,
                query,
                cb
        ) -> {

            Join<Wishlist, Product> product =
                    root.join(
                            "product"
                    );

            Join<Product, Category> category =
                    product.join(
                            "category"
                    );

            Join<Category, Category> parent =
                    category.join(
                            "parent",
                            JoinType.LEFT
                    );

            List<Predicate> conditions =
                    new ArrayList<>();

            // 현재 로그인 회원의 찜만 조회
            conditions.add(
                    cb.equal(
                            root
                                    .get("member")
                                    .get("id"),
                            memberId
                    )
            );

            // 하위 카테고리
            if (
                    categoryId != null
            ) {
                conditions.add(
                        cb.equal(
                                category.get("id"),
                                categoryId
                        )
                );
            }

            // 대분류 + 그 하위 카테고리
            if (
                    mainCategoryId != null
            ) {
                conditions.add(
                        cb.or(
                                cb.equal(
                                        category.get("id"),
                                        mainCategoryId
                                ),
                                cb.equal(
                                        parent.get("id"),
                                        mainCategoryId
                                )
                        )
                );
            }

            // 검색어가 있을 때만 동의어 검색
            if (
                    searchTerms != null &&
                    !searchTerms.isEmpty()
            ) {

                List<Predicate>
                keywordConditions =
                        new ArrayList<>();

                for (
                        String term :
                        searchTerms
                ) {

                    String pattern =
                            "%"
                            + term.toLowerCase()
                            + "%";

                    keywordConditions.add(
                            cb.like(
                                    cb.lower(
                                            product.get("name")
                                    ),
                                    pattern
                            )
                    );

                    keywordConditions.add(
                            cb.like(
                                    cb.lower(
                                            product.get("brand")
                                    ),
                                    pattern
                            )
                    );

                    keywordConditions.add(
                            cb.like(
                                    cb.lower(
                                            category.get("name")
                                    ),
                                    pattern
                            )
                    );

                    keywordConditions.add(
                            cb.like(
                                    cb.lower(
                                            parent.get("name")
                                    ),
                                    pattern
                            )
                    );
                }

                conditions.add(
                        cb.or(
                                keywordConditions
                                        .toArray(
                                                new Predicate[0]
                                        )
                        )
                );
            }

            return cb.and(
                    conditions.toArray(
                            new Predicate[0]
                    )
            );
        };
    }

    // 회원이 실제 찜한 카테고리 목록
    @Transactional(readOnly = true)
    public List<WishlistCategoryResponseDto>
    findCategories(
            Long memberId
    ) {
        return repository
                .findWishlistCategories(memberId)
                .stream()
                .map(
                        WishlistCategoryResponseDto::from
                )
                .toList();
    }

    // 특정 상품 찜 여부
    @Transactional(readOnly = true)
    public boolean exists(
            Long memberId,
            Long productId
    ) {
        return repository
                .existsByMemberIdAndProductId(
                        memberId,
                        productId
                );
    }

    // 회원 찜 개수
    @Transactional(readOnly = true)
    public long countByMemberId(
            Long memberId
    ) {
        return repository
                .countByMemberId(memberId);
    }

    // 상품 찜 개수
    @Transactional(readOnly = true)
    public long countByProductId(
            Long productId
    ) {
        return repository
                .countByProductId(productId);
    }

    // 찜 추가
    @Transactional
    public WishlistResponseDto save(
            WishlistRequestDto requestDto
    ) {
        Long memberId =
                requestDto.getMemberId();

        Long productId =
                requestDto.getProductId();

        if (
                repository
                        .existsByMemberIdAndProductId(
                                memberId,
                                productId
                        )
        ) {
            throw new IllegalArgumentException(
                    "이미 찜한 상품입니다."
            );
        }

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Member not found. id="
                                                + memberId
                                )
                        );

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found. id="
                                                + productId
                                )
                        );

        Wishlist entity =
                Wishlist.builder()
                        .member(member)
                        .product(product)
                        .build();

        return WishlistResponseDto.from(
                repository.save(entity)
        );
    }

    // 찜 삭제
    @Transactional
    public void delete(
            Long memberId,
            Long productId
    ) {
        if (
                !repository
                        .existsByMemberIdAndProductId(
                                memberId,
                                productId
                        )
        ) {
            throw new IllegalArgumentException(
                    "찜 목록에 없는 상품입니다."
            );
        }

        repository
                .deleteByMemberIdAndProductId(
                        memberId,
                        productId
                );
    }
}
