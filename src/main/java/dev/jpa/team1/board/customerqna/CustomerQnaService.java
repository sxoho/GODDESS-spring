package dev.jpa.team1.board.customerqna;

import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.commerce.orders.OrdersRepository;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class CustomerQnaService {

    private final CustomerQnaRepository repository;

    private final MemberRepository memberRepository;

    private final OrdersRepository ordersRepository;


    /**
     * FastAPI JSON 요청/응답 처리
     */
    private final ObjectMapper objectMapper;


    /**
     * 고객 문의 LLM FastAPI 주소
     *
     * application.properties에 값이 없으면
     * 기본적으로 localhost:11102 사용
     */
    @Value("${CUSTOMER_QNA_LLM_BASE_URL:http://localhost:11102}")
    private String customerQnaLlmBaseUrl;



    /**
     * 고객 문의 등록
     */
    @Transactional
    public CustomerQnaResponseDto save(
            CustomerQnaRequestDto requestDto
    ) {

        Member member =
                memberRepository
                        .findById(
                                requestDto.getMemberId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원을 찾을 수 없습니다. memberId="
                                                + requestDto.getMemberId()
                                )
                        );


        Orders orders =
                getOrders(
                        requestDto.getOrderId(),
                        requestDto.getMemberId()
                );


        CustomerQna qna =
                CustomerQna.builder()
                        .member(member)
                        .category(
                                requestDto
                                        .getCategory()
                                        .trim()
                                        .toUpperCase()
                        )
                        .orders(orders)
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
                        .status("WAITING")
                        .build();


        CustomerQna saved =
                repository.save(qna);


        return CustomerQnaResponseDto.from(
                saved
        );
    }



    /**
     * 회원별 고객 문의 전체 조회
     */
    @Transactional(readOnly = true)
    public List<CustomerQnaResponseDto>
    findByMemberId(
            Long memberId
    ) {

        return repository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        CustomerQnaResponseDto::from
                )
                .toList();
    }



    /**
     * 마이페이지 회원별 고객 문의 페이징
     */
    @Transactional(readOnly = true)
    public Page<CustomerQnaResponseDto>
    findMemberPage(
            Long memberId,
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
                        safeSize,
                        Sort.by(
                                Sort.Order.desc(
                                        "createdAt"
                                ),
                                Sort.Order.desc(
                                        "id"
                                )
                        )
                );


        return repository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId,
                        pageable
                )
                .map(
                        CustomerQnaResponseDto::from
                );
    }



    /**
     * 본인 고객 문의 상세
     */
    @Transactional(readOnly = true)
    public CustomerQnaResponseDto
    findMemberDetail(
            Long customerQnaId,
            Long memberId
    ) {

        CustomerQna qna =
                repository
                        .findByIdAndMemberId(
                                customerQnaId,
                                memberId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "고객 문의를 찾을 수 없습니다."
                                )
                        );


        return CustomerQnaResponseDto.from(
                qna
        );
    }



    /**
     * 고객 문의 수정
     *
     * 답변 대기 중인 문의만 수정 가능
     */
    @Transactional
    public CustomerQnaResponseDto update(
            Long customerQnaId,
            CustomerQnaUpdateRequestDto requestDto
    ) {

        CustomerQna qna =
                repository
                        .findByIdAndMemberId(
                                customerQnaId,
                                requestDto.getMemberId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "본인이 작성한 고객 문의를 찾을 수 없습니다."
                                )
                        );


        if (
                !"WAITING".equalsIgnoreCase(
                        qna.getStatus()
                )
        ) {

            throw new IllegalStateException(
                    "답변이 완료된 문의는 수정할 수 없습니다."
            );
        }


        Orders orders =
                getOrders(
                        requestDto.getOrderId(),
                        requestDto.getMemberId()
                );


        qna.update(
                requestDto
                        .getCategory()
                        .trim()
                        .toUpperCase(),

                orders,

                requestDto
                        .getTitle()
                        .trim(),

                requestDto
                        .getContent()
                        .trim()
        );


        return CustomerQnaResponseDto.from(
                qna
        );
    }



    /**
     * 고객 문의 삭제
     *
     * 답변 대기 상태에서만 삭제 가능
     */
    @Transactional
    public void delete(
            Long customerQnaId,
            Long memberId
    ) {

        CustomerQna qna =
                repository
                        .findByIdAndMemberId(
                                customerQnaId,
                                memberId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "본인이 작성한 고객 문의를 찾을 수 없습니다."
                                )
                        );


        if (
                !"WAITING".equalsIgnoreCase(
                        qna.getStatus()
                )
        ) {

            throw new IllegalStateException(
                    "답변이 완료된 문의는 삭제할 수 없습니다."
            );
        }


        repository.delete(qna);
    }



    /**
     * 관리자 고객 문의 상세
     */
    @Transactional(readOnly = true)
    public CustomerQnaResponseDto
    findAdminDetail(
            Long customerQnaId
    ) {

        CustomerQna qna =
                repository
                        .findById(
                                customerQnaId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "고객 문의를 찾을 수 없습니다."
                                )
                        );


        return CustomerQnaResponseDto.from(
                qna
        );
    }



    /**
     * 관리자 고객문의
     *
     * 검색 + 상태필터 + 페이징
     */
    @Transactional(readOnly = true)
    public Page<CustomerQnaResponseDto>
    findAdminPage(
            String keyword,
            String status,
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
                        safeSize,
                        Sort.by(
                                Sort.Order.desc(
                                        "createdAt"
                                ),
                                Sort.Order.desc(
                                        "id"
                                )
                        )
                );


        Specification<CustomerQna>
        specification =
                buildAdminSpecification(
                        keyword,
                        status
                );


        return repository
                .findAll(
                        specification,
                        pageable
                )
                .map(
                        CustomerQnaResponseDto::from
                );
    }



    /**
     * 관리자 고객문의 검색 조건
     *
     * 검색 대상:
     * 제목
     * 내용
     * 문의유형
     * 작성자 닉네임
     */
    private Specification<CustomerQna>
    buildAdminSpecification(
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


            /**
             * 전체가 아닐 경우
             * WAITING / ANSWERED 필터
             */
            if (
                    status != null &&
                    !status.isBlank() &&
                    !"ALL".equalsIgnoreCase(
                            status
                    )
            ) {

                conditions.add(
                        cb.equal(
                                cb.upper(
                                        root.get(
                                                "status"
                                        )
                                ),

                                status
                                        .trim()
                                        .toUpperCase()
                        )
                );
            }


            /**
             * 검색어
             */
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


                Join<CustomerQna, Member> member =
                        root.join(
                                "member",
                                JoinType.INNER
                        );


                Predicate search =
                        cb.or(
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "title"
                                                )
                                        ),
                                        word
                                ),

                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "content"
                                                )
                                        ),
                                        word
                                ),

                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "category"
                                                )
                                        ),
                                        word
                                ),

                                cb.like(
                                        cb.lower(
                                                member.get(
                                                        "nickname"
                                                )
                                        ),
                                        word
                                )
                        );


                conditions.add(
                        search
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
     * 관리자 답변 등록
     *
     * 답변 등록 즉시
     * WAITING -> ANSWERED
     */
    @Transactional
    public CustomerQnaResponseDto answer(
            Long customerQnaId,
            CustomerQnaAnswerRequestDto requestDto
    ) {

        CustomerQna qna =
                repository
                        .findById(
                                customerQnaId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "고객 문의를 찾을 수 없습니다."
                                )
                        );


        qna.answer(
                requestDto
                        .getAnswer()
                        .trim()
        );


        return CustomerQnaResponseDto.from(
                qna
        );
    }



    // =====================================================
    // 고객 문의 AI LLM 분석
    // =====================================================

    /**
     * 회원별 고객 문의 AI 요약
     *
     * CUSTOMER_QNA의 회원별 문의 내역을 가져온 뒤
     * FastAPI -> Ollama -> Gemma2:9b 로 전달하여
     * 마이페이지에 표시할 AI 분석 문장을 생성한다.
     */
    @Transactional(readOnly = true)
    public CustomerQnaAiSummaryDto getAiSummary(
            Long memberId
    ) {

        /**
         * 최신 문의순으로 회원 문의 조회
         */
        List<CustomerQna> qnaList =
                repository
                        .findByMemberIdOrderByCreatedAtDesc(
                                memberId
                        );


        /**
         * 문의가 하나도 없으면
         * FastAPI / Ollama를 호출하지 않는다.
         */
        if (qnaList.isEmpty()) {

            return new CustomerQnaAiSummaryDto(
                    "아직 분석할 고객 문의 내역이 없습니다."
            );
        }


        /**
         * FastAPI에 전달할 고객 문의 데이터를
         * 문자열 형태로 정리
         */
        String qnaText =
                qnaList
                        .stream()
                        .map(qna -> {

                            String answerStatus =

                                    "ANSWERED".equalsIgnoreCase(
                                            qna.getStatus()
                                    )

                                            ? "답변완료"

                                            : "답변대기";


                            return
                                    "[문의 유형] "
                                            + qna.getCategory()

                                            + " | "

                                            + "[제목] "
                                            + qna.getTitle()

                                            + " | "

                                            + "[문의 내용] "
                                            + qna.getContent()

                                            + " | "

                                            + "[상태] "
                                            + answerStatus

                                            + " | "

                                            + "[작성일] "
                                            + qna.getCreatedAt();
                        })
                        .collect(
                                Collectors.joining(
                                        "\n"
                                )
                        );


        try {

            /**
             * FastAPI로 보낼 JSON
             *
             * {
             *     "content": "..."
             * }
             */
            ObjectNode requestBody =
                    objectMapper
                            .createObjectNode();


            requestBody.put(
                    "content",
                    qnaText
            );


            /**
             * FastAPI 요청
             */
            HttpRequest request =
                    HttpRequest
                            .newBuilder()
                            .uri(
                                    URI.create(
                                            customerQnaLlmBaseUrl
                                            + "/customer_qna_summary"
                                    )
                            )
                            .timeout(
                                    Duration.ofSeconds(
                                            120
                                    )
                            )
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest
                                            .BodyPublishers
                                            .ofString(
                                                    objectMapper
                                                            .writeValueAsString(
                                                                    requestBody
                                                            )
                                            )
                            )
                            .build();


            /**
             * HTTP Client
             */
            HttpClient client =
                    HttpClient
                            .newBuilder()
                            .version(
                                    HttpClient.Version.HTTP_1_1
                            )
                            .connectTimeout(
                                    Duration.ofSeconds(
                                            10
                                    )
                            )
                            .build();


            /**
             * FastAPI 호출
             */
            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse
                                    .BodyHandlers
                                    .ofString()
                    );


            /**
             * 200번대 응답이 아니면 오류
             */
            if (
                    response.statusCode() < 200
                            ||
                    response.statusCode() >= 300
            ) {

                throw new IllegalStateException(
                        "고객 문의 AI 요약 서버 응답 오류: "
                                + response.statusCode()
                                + " / "
                                + response.body()
                );
            }


            /**
             * FastAPI 응답
             *
             * {
             *     "result": "최근 고객문의..."
             * }
             */
            JsonNode responseJson =
                    objectMapper
                            .readTree(
                                    response.body()
                            );


            String summary =
                    responseJson
                            .path(
                                    "result"
                            )
                            .asText(
                                    ""
                            )
                            .trim();


            /**
             * 결과가 비어 있는 경우
             */
            if (
                    summary.isBlank()
            ) {

                throw new IllegalStateException(
                        "고객 문의 AI 요약 결과가 비어 있습니다."
                );
            }


            /**
             * React에 반환할 DTO
             */
            return new CustomerQnaAiSummaryDto(
                    summary
            );


        } catch (InterruptedException e) {

            Thread
                    .currentThread()
                    .interrupt();


            throw new IllegalStateException(
                    "고객 문의 AI 요약 요청이 중단되었습니다.",
                    e
            );


        } catch (Exception e) {

            throw new IllegalStateException(
                    "고객 문의 AI 요약 중 오류가 발생했습니다.",
                    e
            );
        }
    }



    /**
     * 회원별 문의 개수
     */
    @Transactional(readOnly = true)
    public long countByMemberId(
            Long memberId
    ) {

        return repository.countByMemberId(
                memberId
        );
    }



    /**
     * 상태별 문의 개수
     */
    @Transactional(readOnly = true)
    public long countByStatus(
            String status
    ) {

        return repository.countByStatus(
                status
                        .trim()
                        .toUpperCase()
        );
    }



    /**
     * 주문 번호가 있을 경우
     * 실제 주문 확인
     *
     * 해당 회원의 주문인지도 확인
     */
    private Orders getOrders(
            Long orderId,
            Long memberId
    ) {

        if (
                orderId == null
        ) {

            return null;
        }


        Orders orders =
                ordersRepository
                        .findById(
                                orderId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "주문을 찾을 수 없습니다. orderId="
                                                + orderId
                                )
                        );


        if (
                orders.getMember() == null
                        ||
                !orders
                        .getMember()
                        .getId()
                        .equals(
                                memberId
                        )
        ) {

            throw new IllegalArgumentException(
                    "본인의 주문만 문의에 연결할 수 있습니다."
            );
        }


        return orders;
    }
}