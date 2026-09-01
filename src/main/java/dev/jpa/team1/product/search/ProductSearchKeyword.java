package dev.jpa.team1.product.search;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 상품 검색 공통 동의어 사전
 *
 * Product / Wishlist 등 상품을 검색하는 기능에서
 * 같은 검색 규칙을 재사용한다.
 */
public final class ProductSearchKeyword {

    private ProductSearchKeyword() {
    }

    /**
     * 같은 줄의 단어들은 서로 같은 의미의 검색어로 취급한다.
     */
    private static final List<List<String>>
    SEARCH_SYNONYM_GROUPS = List.of(
            List.of(
                    "팬츠",
                    "바지",
                    "pants",
                    "jean",
                    "jeans",
                    "trousers"
            ),
            List.of(
                    "상의",
                    "top",
                    "tops"
            ),
            List.of(
                    "셔츠",
                    "남방",
                    "shirt",
                    "shirts"
            ),
            List.of(
                    "티셔츠",
                    "티",
                    "tshirt",
                    "t-shirt",
                    "tee"
            ),
            List.of(
                    "블라우스",
                    "blouse"
            ),
            List.of(
                    "아우터",
                    "outer",
                    "outerwear"
            ),
            List.of(
                    "자켓",
                    "재킷",
                    "jacket"
            ),
            List.of(
                    "코트",
                    "coat"
            ),
            List.of(
                    "스커트",
                    "치마",
                    "skirt"
            ),
            List.of(
                    "원피스",
                    "드레스",
                    "dress",
                    "onepiece",
                    "one-piece"
            ),
            List.of(
                    "신발",
                    "슈즈",
                    "shoe",
                    "shoes"
            ),
            List.of(
                    "모자",
                    "햇",
                    "hat",
                    "cap"
            ),
            List.of(
                    "아이웨어",
                    "안경",
                    "eyewear",
                    "glasses"
            ),
            List.of(
                    "데님",
                    "denim"
            ),
            List.of(
                    "와이드",
                    "wide"
            ),
            List.of(
                    "슬림",
                    "slim"
            ),
            List.of(
                    "블랙",
                    "검정",
                    "black"
            ),
            List.of(
                    "화이트",
                    "흰색",
                    "white"
            ),
            List.of(
                    "블루",
                    "파랑",
                    "blue"
            ),
            List.of(
                    "그레이",
                    "회색",
                    "gray",
                    "grey"
            )
    );

    /**
     * 입력 검색어를 동의어까지 확장한다.
     *
     * 예:
     * 팬츠
     * -> 팬츠, 바지, pants, jean, jeans, trousers
     *
     * wide pants
     * -> wide pants, 와이드 pants, wide 팬츠 ...
     */
    public static List<String> buildSearchTerms(
            String keyword
    ) {

        if (
                keyword == null ||
                keyword.isBlank()
        ) {
            return List.of();
        }

        String normalized =
                keyword
                        .trim()
                        .toLowerCase();

        Set<String> terms =
                new LinkedHashSet<>();

        terms.add(normalized);

        for (
                List<String> group :
                SEARCH_SYNONYM_GROUPS
        ) {

            List<String> currentTerms =
                    new ArrayList<>(terms);

            for (
                    String currentTerm :
                    currentTerms
            ) {

                for (
                        String alias :
                        group
                ) {

                    if (
                            !currentTerm
                                    .contains(alias)
                    ) {
                        continue;
                    }

                    for (
                            String synonym :
                            group
                    ) {
                        terms.add(
                                currentTerm
                                        .replace(
                                                alias,
                                                synonym
                                        )
                        );
                    }
                }
            }
        }

        return terms
                .stream()
                .filter(
                        term ->
                                !term.isBlank()
                )
                .limit(50)
                .toList();
    }
}
