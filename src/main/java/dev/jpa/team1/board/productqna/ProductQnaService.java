package dev.jpa.team1.board.productqna;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQnaService {

    private final ProductQnaRepository repository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * 전체 문의 조회
     */
    @Transactional(readOnly = true)
    public List<ProductQnaResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .sorted(
                        (a, b) ->
                                b.getCreatedAt()
                                        .compareTo(a.getCreatedAt())
                )
                .map(ProductQnaResponseDto::from)
                .toList();
    }

    /**
     * 관리자 상품문의 서버 검색 / 상태 필터 / 페이징
     */
    @Transactional(readOnly = true)
    public Page<ProductQnaResponseDto> findAdminPage(
            String keyword,
            String status,
            int page,
            int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                )
        );

        Specification<ProductQna> specification =
                buildAdminSpecification(
                        keyword,
                        status
                );

        return repository.findAll(
                        specification,
                        pageable
                )
                .map(ProductQnaResponseDto::from);
    }

    /**
     * 관리자 문의 검색 Specification
     *
     * 검색 대상:
     * - 문의 제목
     * - 문의 내용
     * - 상품명
     * - 상품 번호
     * - 작성자 닉네임
     */
    private Specification<ProductQna> buildAdminSpecification(
            String keyword,
            String status
    ) {
        return (
                root,
                query,
                cb
        ) -> {
            List<Predicate> conditions =
                    new ArrayList<>();

            if (
                    status != null &&
                    !status.isBlank() &&
                    !"ALL".equalsIgnoreCase(status)
            ) {
                conditions.add(
                        cb.equal(
                                root.get("status"),
                                status.toUpperCase()
                        )
                );
            }

            if (
                    keyword != null &&
                    !keyword.isBlank()
            ) {
                String word =
                        "%"
                        + keyword
                                .trim()
                                .toLowerCase()
                        + "%";

                Join<ProductQna, Product> product =
                        root.join(
                                "product",
                                JoinType.INNER
                        );

                Join<ProductQna, Member> member =
                        root.join(
                                "member",
                                JoinType.INNER
                        );

                List<Predicate> keywordConditions =
                        new ArrayList<>();

                keywordConditions.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                word
                        )
                );

                keywordConditions.add(
                        cb.like(
                                cb.lower(root.get("content")),
                                word
                        )
                );

                keywordConditions.add(
                        cb.like(
                                cb.lower(product.get("name")),
                                word
                        )
                );

                keywordConditions.add(
                        cb.like(
                                cb.lower(member.get("nickname")),
                                word
                        )
                );

                try {
                    Long productId =
                            Long.parseLong(
                                    keyword.trim()
                            );

                    keywordConditions.add(
                            cb.equal(
                                    product.get("id"),
                                    productId
                            )
                    );
                } catch (NumberFormatException ignored) {
                }

                conditions.add(
                        cb.or(
                                keywordConditions.toArray(
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

    /**
     * 문의 단건 조회
     */
    @Transactional(readOnly = true)
    public ProductQnaResponseDto findById(
            Long productQnaId
    ) {
        ProductQna qna =
                repository.findById(productQnaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "상품 문의를 찾을 수 없습니다. id="
                                                + productQnaId
                                )
                        );

        return ProductQnaResponseDto.from(qna);
    }

    /**
     * 기존 상품별 문의 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ProductQnaPublicResponseDto> findByProductId(
            Long productId,
            Long viewerMemberId
    ) {
        return repository
                .findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(
                        qna ->
                                ProductQnaPublicResponseDto.from(
                                        qna,
                                        viewerMemberId
                                )
                )
                .toList();
    }

    /**
     * 사용자 상품 상세 - 상품별 문의 서버 페이징
     *
     * viewerMemberId:
     * - 로그인 회원 번호
     * - 비로그인은 null
     *
     * 비밀문의는 작성자 본인만 원문을 확인할 수 있다.
     */
    @Transactional(readOnly = true)
    public Page<ProductQnaPublicResponseDto> findProductPage(
            Long productId,
            Long viewerMemberId,
            int page,
            int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize
        );

        return repository
                .findByProductIdOrderByCreatedAtDesc(
                        productId,
                        pageable
                )
                .map(
                        qna ->
                                ProductQnaPublicResponseDto.from(
                                        qna,
                                        viewerMemberId
                                )
                );
    }

    /**
     * 회원별 문의 조회
     */
    @Transactional(readOnly = true)
    public List<ProductQnaResponseDto> findByMemberId(
            Long memberId
    ) {
        return repository
                .findByMemberIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(ProductQnaResponseDto::from)
                .toList();
    }

    /**
     * 마이페이지 회원별 상품문의 서버 페이징
     */
    @Transactional(readOnly = true)
    public Page<ProductQnaResponseDto> findMemberPage(
            Long memberId,
            int page,
            int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                )
        );

        return repository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId,
                        pageable
                )
                .map(ProductQnaResponseDto::from);
    }

    /**
     * 상품별 답변 상태 조회
     */
    @Transactional(readOnly = true)
    public List<ProductQnaResponseDto> findByProductIdAndStatus(
            Long productId,
            String status
    ) {
        return repository
                .findByProductIdAndStatusOrderByCreatedAtDesc(
                        productId,
                        status
                )
                .stream()
                .map(ProductQnaResponseDto::from)
                .toList();
    }

    /**
     * 문의 등록
     */
    @Transactional
    public ProductQnaResponseDto save(
            ProductQnaRequestDto requestDto
    ) {
        Product product =
                productRepository
                        .findById(requestDto.getProductId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "상품을 찾을 수 없습니다. id="
                                                + requestDto.getProductId()
                                )
                        );

        Member member =
                memberRepository
                        .findById(requestDto.getMemberId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원을 찾을 수 없습니다. id="
                                                + requestDto.getMemberId()
                                )
                        );

        String secret =
                normalizeSecret(
                        requestDto.getSecret()
                );

        ProductQna qna =
                ProductQna.builder()
                        .product(product)
                        .member(member)
                        .title(
                                requestDto
                                        .getTitle()
                                        .trim()
                        )
                        .content(
                                requestDto
                                        .getContent()
                                        .trim()
                        )
                        .secret(secret)
                        .status("WAITING")
                        .build();

        return ProductQnaResponseDto.from(
                repository.save(qna)
        );
    }

    /**
     * 문의 수정
     */
    @Transactional
    public ProductQnaResponseDto update(
            Long productQnaId,
            ProductQnaUpdateRequestDto requestDto
    ) {
        ProductQna qna =
                repository
                        .findByIdAndMemberId(
                                productQnaId,
                                requestDto.getMemberId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "본인이 작성한 문의가 아니거나 문의를 찾을 수 없습니다."
                                )
                        );


        qna.update(
                requestDto.getTitle().trim(),
                requestDto.getContent().trim(),
                normalizeSecret(
                        requestDto.getSecret()
                )
        );

        return ProductQnaResponseDto.from(qna);
    }

    /**
     * 문의 삭제
     */
    @Transactional
    public void delete(
            Long productQnaId,
            Long memberId
    ) {
        ProductQna qna =
                repository
                        .findByIdAndMemberId(
                                productQnaId,
                                memberId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "본인이 작성한 문의가 아니거나 문의를 찾을 수 없습니다."
                                )
                        );


        repository.delete(qna);
    }

    /**
     * 관리자 답변 등록/수정
     */
    @Transactional
    public ProductQnaResponseDto answer(
            Long productQnaId,
            ProductQnaAnswerRequestDto requestDto
    ) {
        ProductQna qna =
                repository.findById(productQnaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "상품 문의를 찾을 수 없습니다. id="
                                                + productQnaId
                                )
                        );

        if (
                "CLOSED".equals(
                        qna.getStatus()
                )
        ) {
            throw new IllegalStateException(
                    "종료된 문의에는 답변할 수 없습니다."
            );
        }

        Member answerer =
                memberRepository
                        .findById(requestDto.getAnswererId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "답변자를 찾을 수 없습니다. id="
                                                + requestDto.getAnswererId()
                                )
                        );

        qna.answer(
                answerer,
                requestDto
                        .getAnswer()
                        .trim()
        );

        return ProductQnaResponseDto.from(qna);
    }

    /**
     * 문의 종료
     */
    @Transactional
    public ProductQnaResponseDto close(
            Long productQnaId
    ) {
        ProductQna qna =
                repository.findById(productQnaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "상품 문의를 찾을 수 없습니다. id="
                                                + productQnaId
                                )
                        );

        qna.close();

        return ProductQnaResponseDto.from(qna);
    }

    @Transactional(readOnly = true)
    public long countByProductId(
            Long productId
    ) {
        return repository.countByProductId(productId);
    }

    @Transactional(readOnly = true)
    public long countByMemberId(
            Long memberId
    ) {
        return repository.countByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public long countByStatus(
            String status
    ) {
        return repository.countByStatus(status);
    }

    private String normalizeSecret(
            String secret
    ) {
        if (
                "Y".equalsIgnoreCase(secret)
        ) {
            return "Y";
        }

        return "N";
    }
}