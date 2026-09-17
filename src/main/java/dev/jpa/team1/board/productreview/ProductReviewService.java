package dev.jpa.team1.board.productreview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.jpa.team1.beauty.save.BeautySave;
import dev.jpa.team1.beauty.save.BeautySaveRepository;
import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BeautySaveRepository beautySaveRepository;
    private final ProductReviewAiSummaryCacheRepository aiSummaryCacheRepository;
    private final ObjectMapper objectMapper;

    @Value("${PRODUCT_REVIEW_LLM_BASE_URL:http://139.150.91.194:11550}")
    private String productReviewLlmBaseUrl;

    @Value("${SpringBoot_FastAPI_KEY:}")
    private String fastApiKey;

    /**
     * 구매한 주문 상세를 기준으로 리뷰 등록
     *
     * PRODUCT_ID는 클라이언트 값을 신뢰하지 않고
     * ORDER_DETAIL -> PRODUCT_OPTION -> PRODUCT 관계에서 결정한다.
     */
    @Transactional
    public ProductReviewResponseDto save(ProductReviewRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        OrderDetail orderDetail = orderDetailRepository
                .findById(requestDto.getOrderDetailId())
                .orElseThrow(() -> new IllegalArgumentException("주문 상세 정보를 찾을 수 없습니다."));

        if (!orderDetail.getOrders().getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인이 구매한 상품에만 리뷰를 작성할 수 있습니다.");
        }

        if (!"DELIVERED".equals(orderDetail.getOrders().getStatus())) {
            throw new IllegalArgumentException("배송 완료된 상품에만 리뷰를 작성할 수 있습니다.");
        }

        if (productReviewRepository.existsByOrderDetailId(orderDetail.getId())) {
            throw new IllegalArgumentException("이미 리뷰를 작성한 주문 상품입니다.");
        }

        Product product = orderDetail
                .getProductOption()
                .getProduct();

        BeautySave beautySave = getBeautySave(
                requestDto.getBeautySaveId(),
                requestDto.getMemberId()
        );

        ProductReview review = ProductReview.builder()
                .member(member)
                .orderDetail(orderDetail)
                .product(product)
                .beautySave(beautySave)
                .title(requestDto.getTitle().trim())
                .content(requestDto.getContent().trim())
                .rating(requestDto.getRating())
                .visible("Y")
                .build();

        ProductReview saved = productReviewRepository.save(review);

        // 리뷰 데이터가 바뀌었으므로 기존 AI 요약 캐시 무효화
        invalidateAiSummary(product.getId());

        return ProductReviewResponseDto.from(saved);
    }

    public ProductReviewResponseDto findById(Long reviewId) {
        return ProductReviewResponseDto.from(findReview(reviewId));
    }

    /**
     * 주문상품에 이미 리뷰가 작성되어 있는지 확인한다.
     */
    public boolean existsByOrderDetailId(Long orderDetailId) {
        return productReviewRepository.existsByOrderDetailId(orderDetailId);
    }

    public Page<ProductReviewPublicResponseDto> findProductPage(
            Long productId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return productReviewRepository
                .findByProductIdAndVisible(productId, "Y", pageable)
                .map(ProductReviewPublicResponseDto::from);
    }

    public Page<ProductReviewResponseDto> findMemberPage(
            Long memberId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return productReviewRepository
                .findByMemberId(memberId, pageable)
                .map(ProductReviewResponseDto::from);
    }

    public Page<ProductReviewResponseDto> findAdminPage(
            String keyword,
            String visible,
            Integer rating,
            int page,
            int size
    ) {
        String normalizedKeyword = normalize(keyword);
        String normalizedVisible = normalize(visible);

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return productReviewRepository
                .findAdminPage(
                        normalizedKeyword,
                        normalizedVisible,
                        rating,
                        pageable
                )
                .map(ProductReviewResponseDto::from);
    }

    @Transactional
    public ProductReviewResponseDto update(
            Long reviewId,
            ProductReviewUpdateRequestDto requestDto
    ) {
        ProductReview review = productReviewRepository
                .findByIdAndMemberId(reviewId, requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("본인이 작성한 리뷰만 수정할 수 있습니다."));

        BeautySave beautySave = getBeautySave(
                requestDto.getBeautySaveId(),
                requestDto.getMemberId()
        );

        review.update(
                requestDto.getTitle().trim(),
                requestDto.getContent().trim(),
                requestDto.getRating(),
                beautySave
        );

        invalidateAiSummary(review.getProduct().getId());

        return ProductReviewResponseDto.from(review);
    }

    @Transactional
    public void delete(Long reviewId, Long memberId) {
        ProductReview review = productReviewRepository
                .findByIdAndMemberId(reviewId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다."));

        Long productId = review.getProduct().getId();

        productReviewRepository.delete(review);

        invalidateAiSummary(productId);
    }

    @Transactional
    public ProductReviewResponseDto changeVisible(
            Long reviewId,
            ProductReviewVisibleRequestDto requestDto
    ) {
        ProductReview review = findReview(reviewId);

        review.changeVisible(requestDto.getVisible());

        invalidateAiSummary(review.getProduct().getId());

        return ProductReviewResponseDto.from(review);
    }

    /**
     * 상품별 AI 리뷰 요약
     *
     * 1) 캐시가 있으면 DB의 요약을 즉시 반환한다.
     * 2) 리뷰 등록/수정/삭제/공개상태 변경 시 캐시를 삭제한다.
     * 3) 캐시가 없는 첫 조회에서만 FastAPI LLM을 호출하고 결과를 다시 저장한다.
     *
     * 따라서 새로고침할 때마다 LLM을 재호출하지 않는다.
     */
    @Transactional
    public ProductReviewAiSummaryDto getAiSummary(Long productId) {
        ProductReviewAiSummaryCache cached = aiSummaryCacheRepository
                .findById(productId)
                .orElse(null);

        if (cached != null) {
            return new ProductReviewAiSummaryDto(cached.getSummary());
        }

        Pageable pageable = PageRequest.of(
                0,
                50,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<ProductReview> reviewPage = productReviewRepository
                .findByProductIdAndVisible(
                        productId,
                        "Y",
                        pageable
                );

        if (reviewPage.isEmpty()) {
            return new ProductReviewAiSummaryDto(
                    "아직 요약할 리뷰가 없습니다."
            );
        }

        String reviewText = reviewPage.getContent()
                .stream()
                .map(review ->
                        "[별점 " + review.getRating() + "점] "
                                + review.getTitle() + " - "
                                + review.getContent()
                )
                .collect(Collectors.joining("\n"));

        String summary =
                requestAiSummary(
                        productId,
                        reviewText
                );;

        aiSummaryCacheRepository.save(
                ProductReviewAiSummaryCache.builder()
                        .productId(productId)
                        .summary(summary)
                        .reviewCount(reviewPage.getTotalElements())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        return new ProductReviewAiSummaryDto(summary);
    }

    public ProductReviewSummaryDto getSummary(Long productId) {
        long count = productReviewRepository
                .countByProductIdAndVisible(productId, "Y");

        Double average = productReviewRepository
                .findAverageRatingByProductId(productId);

        double roundedAverage = Math.round(
                (average == null ? 0.0 : average) * 10.0
        ) / 10.0;

        return new ProductReviewSummaryDto(
                count,
                roundedAverage
        );
    }

    private String requestAiSummary(
            Long productId,
            String reviewText
    ) {
        try {
        	
        	 System.out.println(
                     "Spring FastAPI Key = "
                             + (
                                 fastApiKey != null && fastApiKey.length() >= 4
                                 ? fastApiKey.substring(0, 4) + "****"
                                 : "미설정"
                             )
             );
        	 
            ObjectNode requestBody = objectMapper.createObjectNode();

            requestBody.put(
                    "SpringBoot_FastAPI_KEY",
                    fastApiKey
            );

            requestBody.put(
                    "content",
                    reviewText
            );
            
            requestBody.put(
                    "product_id",
                    productId
            );

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(
                            productReviewLlmBaseUrl + "/review-summary"
                    ))
                    .timeout(Duration.ofSeconds(180))
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .POST(
                            HttpRequest.BodyPublishers.ofString(
                                    objectMapper.writeValueAsString(
                                            requestBody
                                    )
                            )
                    )
                    .build();

            HttpClient client = HttpClient
                    .newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            
            System.out.println(
                    "AI SUMMARY 요청 URL = "
                    + productReviewLlmBaseUrl
                    + "/review-summary"
            );

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 ||
                    response.statusCode() >= 300) {

                System.out.println(
                        "FastAPI 응답 코드: "
                                + response.statusCode()
                );

                System.out.println(
                        "FastAPI 응답 내용: "
                                + response.body()
                );

                throw new IllegalStateException(
                        "AI 리뷰 요약 서버 응답 오류: "
                                + response.statusCode()
                                + " / "
                                + response.body()
                );
            }

            JsonNode responseJson = objectMapper.readTree(
                    response.body()
            );

            String summary = responseJson
                    .path("result")
                    .asText("")
                    .trim();

            if (summary.isBlank()) {
                throw new IllegalStateException(
                        "AI 리뷰 요약 결과가 비어 있습니다."
                );
            }

            return summary;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "AI 리뷰 요약 요청이 중단되었습니다.",
                    e
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "AI 리뷰 요약 중 오류가 발생했습니다.",
                    e
            );
            
            
        }
        
        
    }

    /**
     * 리뷰 변경 시 기존 요약을 삭제한다.
     * 다음 AI 요약 조회에서만 다시 분석한다.
     */
    private void invalidateAiSummary(Long productId) {
        aiSummaryCacheRepository
                .findById(productId)
                .ifPresent(aiSummaryCacheRepository::delete);
    }

    private ProductReview findReview(Long reviewId) {
        return productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
    }

    private BeautySave getBeautySave(
            Long beautySaveId,
            Long memberId
    ) {
        if (beautySaveId == null) {
            return null;
        }

        BeautySave beautySave = beautySaveRepository.findById(beautySaveId)
                .orElseThrow(() -> new IllegalArgumentException("저장한 뷰티 체험 결과를 찾을 수 없습니다."));

        if (!beautySave.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 뷰티 체험 결과만 리뷰에 첨부할 수 있습니다.");
        }

        return beautySave;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
