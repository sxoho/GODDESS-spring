package dev.jpa.team1.board.productreview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
import dev.jpa.team1.beauty.save.BeautySave;
import dev.jpa.team1.beauty.save.BeautySaveRepository;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BeautySaveRepository beautySaveRepository;
    private final ObjectMapper objectMapper;

    private String productReviewLlmBaseUrl =
            "http://139.150.91.194:11550";

    @Value("${SpringBoot_FastAPI_KEY:}")
    private String fastApiKey;

    @Transactional
    public ProductReviewResponseDto save(ProductReviewRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        OrderDetail orderDetail = null;
        if (requestDto.getOrderDetailId() != null) {
            if (productReviewRepository.existsByOrderDetailId(requestDto.getOrderDetailId())) {
                throw new IllegalArgumentException("이미 리뷰를 작성한 주문 상품입니다.");
            }

            orderDetail = orderDetailRepository.findById(requestDto.getOrderDetailId())
                    .orElseThrow(() -> new IllegalArgumentException("주문 상세 정보를 찾을 수 없습니다."));
        }

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

        return ProductReviewResponseDto.from(
                productReviewRepository.save(review)
        );
    }

    public ProductReviewResponseDto findById(Long reviewId) {
        return ProductReviewResponseDto.from(
                findReview(reviewId)
        );
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

        return ProductReviewResponseDto.from(review);
    }

    @Transactional
    public void delete(Long reviewId, Long memberId) {
        ProductReview review = productReviewRepository
                .findByIdAndMemberId(reviewId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다."));

        productReviewRepository.delete(review);
    }

    @Transactional
    public ProductReviewResponseDto changeVisible(
            Long reviewId,
            ProductReviewVisibleRequestDto requestDto
    ) {
        ProductReview review = findReview(reviewId);
        review.changeVisible(requestDto.getVisible());
        return ProductReviewResponseDto.from(review);
    }

    /**
     * 상품별 AI 리뷰 요약
     *
     * 공개 리뷰 최대 50개를 모아 FastAPI LLM 요약 API로 전달한다.
     */
    public ProductReviewAiSummaryDto getAiSummary(Long productId) {
    	
    	System.out.println("LLM BASE URL = " + productReviewLlmBaseUrl);
    	System.out.println("LLM REQUEST URL = " + productReviewLlmBaseUrl + "/review-summary");
    	
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

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put(
                    "SpringBoot_FastAPI_KEY",
                    fastApiKey
            );
            requestBody.put(
                    "content",
                    reviewText
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

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 ||
                    response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "AI 리뷰 요약 서버 응답 오류: "
                                + response.statusCode()
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

            return new ProductReviewAiSummaryDto(
                    summary
            );

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