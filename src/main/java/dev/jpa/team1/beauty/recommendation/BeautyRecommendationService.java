package dev.jpa.team1.beauty.recommendation;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productcolorimage.ProductColorImage;
import dev.jpa.team1.product.productcolorimage.ProductColorImageRepository;
import dev.jpa.team1.recommend.recommendproduct.RecommendProduct;
import dev.jpa.team1.recommend.recommendproduct.RecommendProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BeautyRecommendationService {

    private static final int DEFAULT_LIMIT = 6;
    private static final int MAX_LIMIT = 20;

    private final BeautyRecommendationRepository beautyRecommendationRepository;
    private final BeautyRecommendationQueryRepository beautyRecommendationQueryRepository;
    private final RecommendProductRepository recommendProductRepository;
    private final ProductColorImageRepository productColorImageRepository;
    private final MemberRepository memberRepository;

    /**
     * 추천 화면의 "내 정보 불러오기"에서 사용.
     */
    @Transactional(readOnly = true)
    public BeautyRecommendationProfileDto getMemberProfile(Long memberId) {
        Member member = findMember(memberId);
        return BeautyRecommendationProfileDto.from(member);
    }

    /**
     * 조건 기반 뷰티 상품 추천 생성.
     *
     * 1. 회원 조회
     * 2. 요청값이 없으면 회원의 현재 피부타입/퍼스널컬러 사용
     * 3. 퍼스널컬러에 맞는 PRODUCT_COLOR 후보 조회
     * 4. 피부타입/상품타입 조건 필터링
     * 5. BEAUTY_RECOMMENDATION 이력 저장
     * 6. RECOMMEND_PRODUCT 추천 상품 저장
     */
    @Transactional
    public BeautyRecommendationResponseDto createRecommendation(
            BeautyRecommendationRequestDto requestDto
    ) {
        if (requestDto == null || requestDto.getMemberId() == null) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "회원 번호가 필요합니다."
            );
        }

        Member member = findMember(requestDto.getMemberId());

        String personalColor = firstNotBlank(
                requestDto.getPersonalColor(),
                member.getPersonalColor()
        );

        String skinType = firstNotBlank(
                requestDto.getSkinType(),
                member.getSkinType()
        );

        String recommendationType = firstNotBlank(
                requestDto.getRecommendationType(),
                "COMBINED"
        );

        String mood = trimToNull(requestDto.getMood());
        String itemType = trimToNull(requestDto.getItemType());
        int limit = normalizeLimit(requestDto.getLimit());

        List<ProductColor> candidates = personalColor == null
                ? beautyRecommendationQueryRepository.findAllAvailable()
                : beautyRecommendationQueryRepository.findByPersonalColor(personalColor);

        /*
         * 같은 상품에 여러 색상이 매칭돼도 상품은 한 번만 추천한다.
         * 퍼스널컬러 조회 결과의 첫 번째 색상을 대표 추천 색상으로 사용한다.
         */
        Map<Long, ProductColor> uniqueProductColors = new LinkedHashMap<>();

        for (ProductColor productColor : candidates) {
            Product product = productColor.getProduct();

            if (!matchesItemType(product.getProductType(), itemType)) {
                continue;
            }

            if (!matchesSkinType(product.getSkinType(), skinType)) {
                continue;
            }

            uniqueProductColors.putIfAbsent(
                    product.getId(),
                    productColor
            );

            if (uniqueProductColors.size() >= limit) {
                break;
            }
        }

        String aiDescription = buildSummary(
                personalColor,
                skinType,
                itemType,
                uniqueProductColors.size()
        );

        BeautyRecommendation recommendation =
                beautyRecommendationRepository.save(
                        BeautyRecommendation.builder()
                                .member(member)
                                .recommendationType(recommendationType)
                                .mood(mood)
                                .skinType(skinType)
                                .personalColor(personalColor)
                                .aiDescription(aiDescription)
                                .build()
                );

        List<BeautyRecommendationProductDto> products = new ArrayList<>();
        int sortOrder = 1;

        for (ProductColor productColor : uniqueProductColors.values()) {
            Product product = productColor.getProduct();

            String reason = buildReason(
                    personalColor,
                    skinType,
                    productColor
            );

            RecommendProduct saved =
                    recommendProductRepository.save(
                            RecommendProduct.builder()
                                    .beautyRecommendation(recommendation)
                                    .product(product)
                                    .productColor(productColor)
                                    .itemType(
                                            itemType != null
                                                    ? itemType
                                                    : product.getProductType()
                                    )
                                    .sortOrder(sortOrder)
                                    .reason(reason)
                                    .build()
                    );

            products.add(
                    toProductDto(saved)
            );

            sortOrder++;
        }

        return toResponseDto(
                recommendation,
                products
        );
    }

    /**
     * 회원의 추천 이력 조회.
     */
    @Transactional(readOnly = true)
    public List<BeautyRecommendationResponseDto> getMemberHistory(
            Long memberId
    ) {
        findMember(memberId);

        return beautyRecommendationRepository
                .findByMemberIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(this::toResponseDtoWithProducts)
                .toList();
    }

    /**
     * 추천 이력 한 건 조회.
     */
    @Transactional(readOnly = true)
    public BeautyRecommendationResponseDto getRecommendation(
            Long recommendationId
    ) {
        BeautyRecommendation recommendation =
                beautyRecommendationRepository
                        .findById(recommendationId)
                        .orElseThrow(
                                () -> new CustomException(
                                        ErrorCode.INVALID_INPUT_VALUE,
                                        "추천 이력을 찾을 수 없습니다."
                                )
                        );

        return toResponseDtoWithProducts(recommendation);
    }

    private BeautyRecommendationResponseDto toResponseDtoWithProducts(
            BeautyRecommendation recommendation
    ) {
        List<BeautyRecommendationProductDto> products =
                recommendProductRepository
                        .findByBeautyRecommendationIdOrderBySortOrderAsc(
                                recommendation.getId()
                        )
                        .stream()
                        .map(this::toProductDto)
                        .toList();

        return toResponseDto(
                recommendation,
                products
        );
    }

    private BeautyRecommendationResponseDto toResponseDto(
            BeautyRecommendation recommendation,
            List<BeautyRecommendationProductDto> products
    ) {
        return new BeautyRecommendationResponseDto(
                recommendation.getId(),
                recommendation.getMember().getId(),
                recommendation.getRecommendationType(),
                recommendation.getMood(),
                recommendation.getSkinType(),
                recommendation.getPersonalColor(),
                recommendation.getAiDescription(),
                recommendation.getCreatedAt(),
                products
        );
    }

    private BeautyRecommendationProductDto toProductDto(
            RecommendProduct recommendProduct
    ) {
        Product product = recommendProduct.getProduct();
        ProductColor productColor = recommendProduct.getProductColor();

        String imagePath = findRepresentativeImagePath(productColor);

        return new BeautyRecommendationProductDto(
                recommendProduct.getId(),
                product.getId(),
                productColor != null ? productColor.getId() : null,
                product.getName(),
                product.getBrand(),
                product.getProductType(),
                product.getPrice(),
                product.getDiscount(),
                productColor != null
                        ? productColor.getColorMaster().getColorName()
                        : null,
                productColor != null
                        ? productColor.getColorMaster().getColorCode()
                        : null,
                productColor != null
                        ? productColor.getColorMaster().getBaseColorGroup()
                        : null,
                imagePath,
                recommendProduct.getItemType(),
                recommendProduct.getSortOrder(),
                recommendProduct.getReason()
        );
    }

    private String findRepresentativeImagePath(
            ProductColor productColor
    ) {
        if (productColor == null) {
            return null;
        }

        return productColorImageRepository
                .findFirstByProductColorIdAndIsMainOrderBySortOrderAsc(
                        productColor.getId(),
                        "Y"
                )
                .map(ProductColorImage::getFilePath)
                .orElseGet(
                        () -> productColorImageRepository
                                .findByProductColorIdOrderBySortOrderAsc(
                                        productColor.getId()
                                )
                                .stream()
                                .findFirst()
                                .map(ProductColorImage::getFilePath)
                                .orElse(null)
                );
    }

    private Member findMember(Long memberId) {
        if (memberId == null) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "회원 번호가 필요합니다."
            );
        }

        return memberRepository
                .findById(memberId)
                .orElseThrow(
                        () -> new CustomException(
                                ErrorCode.MEMBER_NOT_FOUND
                        )
                );
    }

    private boolean matchesItemType(
            String productType,
            String requestedItemType
    ) {
        if (requestedItemType == null) {
            return true;
        }

        if (productType == null || productType.isBlank()) {
            return false;
        }

        return normalize(productType)
                .contains(normalize(requestedItemType));
    }

    /**
     * PRODUCT.SKIN_TYPE이 비어 있으면 모든 피부 타입이 사용할 수 있는 상품으로 본다.
     *
     * MEMBER는 DRY/OILY 같은 코드값을 사용하고,
     * 상품 데이터에는 건성/지성 같은 한글 값이 들어갈 수 있어 둘 다 비교한다.
     */
    private boolean matchesSkinType(
            String productSkinType,
            String requestedSkinType
    ) {
        if (requestedSkinType == null) {
            return true;
        }

        if (productSkinType == null || productSkinType.isBlank()) {
            return true;
        }

        String productValue = normalize(productSkinType);

        for (String alias : skinTypeAliases(requestedSkinType)) {
            if (productValue.contains(normalize(alias))) {
                return true;
            }
        }

        return false;
    }

    private List<String> skinTypeAliases(String skinType) {
        String normalized = normalize(skinType);

        if (normalized.contains("DRY") || normalized.contains("건성")) {
            return List.of("DRY", "건성");
        }
        if (normalized.contains("OILY") || normalized.contains("지성")) {
            return List.of("OILY", "지성");
        }
        if (normalized.contains("COMBINATION") || normalized.contains("복합")) {
            return List.of("COMBINATION", "복합성", "복합");
        }
        if (normalized.contains("SENSITIVE") || normalized.contains("민감")) {
            return List.of("SENSITIVE", "민감성", "민감");
        }
        if (normalized.contains("NORMAL") || normalized.contains("중성")) {
            return List.of("NORMAL", "중성");
        }
        if (normalized.contains("DEHYDRATED") || normalized.contains("수부지") || normalized.contains("속건조")) {
            return List.of("DEHYDRATED", "수부지", "속건조");
        }

        return List.of(skinType);
    }

    private String buildReason(
            String personalColor,
            String skinType,
            ProductColor productColor
    ) {
        List<String> reasons = new ArrayList<>();

        if (personalColor != null) {
            reasons.add(
                    personalColor + " 퍼스널컬러와 매칭된 "
                            + productColor.getColorMaster().getColorName()
                            + " 색상"
            );
        }

        if (skinType != null) {
            reasons.add(
                    skinType + " 피부 타입 조건 반영"
            );
        }

        if (reasons.isEmpty()) {
            return "현재 판매중인 뷰티 상품 중 추천된 상품입니다.";
        }

        return String.join(", ", reasons) + " 상품입니다.";
    }

    private String buildSummary(
            String personalColor,
            String skinType,
            String itemType,
            int productCount
    ) {
        List<String> conditions = new ArrayList<>();

        if (personalColor != null) {
            conditions.add("퍼스널컬러 " + personalColor);
        }
        if (skinType != null) {
            conditions.add("피부타입 " + skinType);
        }
        if (itemType != null) {
            conditions.add("상품타입 " + itemType);
        }

        if (conditions.isEmpty()) {
            return "현재 판매중인 상품을 기준으로 "
                    + productCount
                    + "개의 상품을 추천했습니다.";
        }

        return String.join(", ", conditions)
                + " 조건을 기준으로 "
                + productCount
                + "개의 상품을 추천했습니다.";
    }

    private int normalizeLimit(Integer requestedLimit) {
        if (requestedLimit == null) {
            return DEFAULT_LIMIT;
        }

        if (requestedLimit < 1) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "추천 상품 개수는 1개 이상이어야 합니다."
            );
        }

        return Math.min(requestedLimit, MAX_LIMIT);
    }

    private String firstNotBlank(String first, String second) {
        String firstValue = trimToNull(first);
        if (firstValue != null) {
            return firstValue;
        }
        return trimToNull(second);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim().toUpperCase(Locale.ROOT);
    }
}
