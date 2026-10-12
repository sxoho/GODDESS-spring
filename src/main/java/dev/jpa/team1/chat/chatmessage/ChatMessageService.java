package dev.jpa.team1.chat.chatmessage;

import dev.jpa.team1.chat.chatroom.ChatRoom;
import dev.jpa.team1.chat.chatroom.ChatRoomRepository;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;

import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productcolor.ProductColorRepository;

import dev.jpa.team1.product.productcolorimage.ProductColorImage;
import dev.jpa.team1.product.productcolorimage.ProductColorImageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final MemberRepository memberRepository;

    private final ChatbotAiService chatbotAiService;

    private final ProductRepository productRepository;

    private final ProductColorRepository productColorRepository;

    private final ProductColorImageRepository productColorImageRepository;


    /*
     * =========================================
     * USER 메시지 저장
     * =========================================
     */
    @Transactional
    public ChatMessageResponseDto sendUserMessage(
            Long memberId,
            Long roomId,
            ChatMessageSendRequestDto requestDto
    ) {

        ChatRoom chatRoom =
                getAccessibleActiveRoom(
                        memberId,
                        roomId
                );


        ChatMessage message =
                ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .senderType("USER")
                        .message(
                                requestDto
                                        .getMessage()
                                        .trim()
                        )
                        .build();


        return ChatMessageResponseDto.from(
                chatMessageRepository.save(
                        message
                )
        );
    }


    /*
     * =========================================
     * 채팅방 메시지 전체 조회
     * =========================================
     */
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getMessages(
            Long memberId,
            Long roomId
    ) {

        getAccessibleRoom(
                memberId,
                roomId
        );


        return chatMessageRepository
                .findByChatRoomIdOrderByIdAsc(
                        roomId
                )
                .stream()
                .map(
                        ChatMessageResponseDto::from
                )
                .toList();
    }


    /*
     * =========================================
     * BOT 메시지 저장
     * =========================================
     */
    @Transactional
    public ChatMessageResponseDto saveBotMessage(
            Long roomId,
            String answer
    ) {

        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(
                                roomId
                        )
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.CHAT_ROOM_NOT_FOUND
                                        )
                        );


        if (
                !"ACTIVE".equals(
                        chatRoom.getStatus()
                )
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "종료된 챗봇 대화에는 메시지를 저장할 수 없습니다."
            );
        }


        ChatMessage message =
                ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .senderType("BOT")
                        .message(answer)
                        .build();


        return ChatMessageResponseDto.from(
                chatMessageRepository.save(
                        message
                )
        );
    }


    /*
     * =========================================
     * AI 챗봇 질문
     *
     * 1. 회원 조회
     * 2. ACTIVE 채팅방 확인
     * 3. USER 질문 저장
     * 4. FastAPI 챗봇 호출
     * 5. 실제 상품 조회
     * 6. BOT 답변 저장
     * 7. React 응답
     * =========================================
     */
    @Transactional
    public ChatbotAskResponseDto askChatbot(
            Long memberId,
            Long roomId,
            ChatbotAskRequestDto requestDto
    ) {

        /*
         * =====================================
         * 회원 조회
         * =====================================
         */
        Member member =
                memberRepository
                        .findById(
                                memberId
                        )
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.MEMBER_NOT_FOUND
                                        )
                        );


        /*
         * =====================================
         * ACTIVE 채팅방 확인
         * =====================================
         */
        ChatRoom chatRoom =
                getAccessibleActiveRoom(
                        memberId,
                        roomId
                );


        String question =
                requestDto
                        .getMessage()
                        .trim();


        /*
         * =====================================
         * USER 질문 저장
         * =====================================
         */
        ChatMessage userEntity =
                ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .senderType("USER")
                        .message(question)
                        .build();


        ChatMessageResponseDto userMessage =
                ChatMessageResponseDto.from(
                        chatMessageRepository.save(
                                userEntity
                        )
                );


        /*
         * =====================================
         * 회원 개인화 정보
         * =====================================
         */
        String skinType =
                member.getSkinType();


        String personalColor =
                member.getPersonalColor();


        /*
         * =====================================
         * FastAPI 챗봇 호출
         * =====================================
         */
        ChatbotAiService.AiAnswer aiAnswer =
                chatbotAiService.ask(
                        roomId,
                        question,
                        skinType,
                        personalColor
                );


        /*
         * =====================================
         * 실제 여신강림 상품 추천
         * =====================================
         */
        List<ChatbotRecommendedProductDto>
                recommendedProducts =
                findRecommendedProducts(
                        aiAnswer.getProductRecommendation()
                );


        /*
         * =====================================
         * BOT 답변 저장
         * =====================================
         */
        ChatMessage botEntity =
                ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .senderType("BOT")
                        .message(
                                aiAnswer.getMessage()
                        )
                        .build();


        ChatMessageResponseDto botMessage =
                ChatMessageResponseDto.from(
                        chatMessageRepository.save(
                                botEntity
                        )
                );


        /*
         * =====================================
         * React 응답
         * =====================================
         */
        return ChatbotAskResponseDto
                .builder()
                .userMessage(
                        userMessage
                )
                .botMessage(
                        botMessage
                )
                .action(
                        aiAnswer.getAction()
                )
                .recommendedProducts(
                        recommendedProducts
                )
                .build();
    }


    /*
     * =========================================
     * ACTIVE 채팅방 검사
     * =========================================
     */
    private ChatRoom getAccessibleActiveRoom(
            Long memberId,
            Long roomId
    ) {

        ChatRoom chatRoom =
                getAccessibleRoom(
                        memberId,
                        roomId
                );


        if (
                !"ACTIVE".equals(
                        chatRoom.getStatus()
                )
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "종료된 챗봇 대화에는 메시지를 보낼 수 없습니다."
            );
        }


        return chatRoom;
    }


    /*
     * =========================================
     * 채팅방 접근 권한 검사
     * =========================================
     */
    private ChatRoom getAccessibleRoom(
            Long memberId,
            Long roomId
    ) {

        /*
         * 회원 존재 확인
         */
        memberRepository
                .findById(
                        memberId
                )
                .orElseThrow(
                        () ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                );


        /*
         * 채팅방 조회
         */
        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(
                                roomId
                        )
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.CHAT_ROOM_NOT_FOUND
                                        )
                        );


        /*
         * 다른 회원의 채팅방 접근 방지
         */
        if (
                !chatRoom
                        .getMember()
                        .getId()
                        .equals(
                                memberId
                        )
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_CHAT_ACCESS
            );
        }


        return chatRoom;
    }


    /*
     * =========================================
     * 실제 상품 추천
     * =========================================
     */
    private List<ChatbotRecommendedProductDto>
    findRecommendedProducts(
            ChatbotAiService.ProductRecommendation condition
    ) {

        /*
         * 상품 추천 질문이 아닌 경우
         */
        if (condition == null) {

            return List.of();
        }


        String personalColor =
                condition.getPersonalColor();


        String productType =
                condition.getProductType();


        int limit =
                condition.getLimit() == null
                        ? 3
                        : condition.getLimit();


        /*
         * 비정상 limit 방어
         */
        if (limit <= 0) {

            limit = 3;
        }


        /*
         * 추천 조건 부족
         */
        if (
                personalColor == null
                ||
                personalColor.isBlank()
                ||
                productType == null
                ||
                productType.isBlank()
        ) {

            return List.of();
        }


        /*
         * =====================================
         * 상품 조회
         *
         * MAKEUP
         * → 전체 판매 상품 중
         *   LIP / BASE / CHEEK / EYE
         *
         * 특정 상품 종류
         * → 해당 productType만 조회
         * =====================================
         */
        List<Product> products;


        if (
                "MAKEUP".equalsIgnoreCase(
                        productType
                )
        ) {

            products =
                    productRepository
                            .findByStatusOrderByCreatedAtDesc(
                                    "ON_SALE"
                            );

        } else {

            products =
                    productRepository
                            .findByProductTypeIgnoreCaseAndStatusOrderByViewCountDesc(
                                    productType,
                                    "ON_SALE"
                            );
        }


        List<ChatbotRecommendedProductDto> result =
                new ArrayList<>();


        /*
         * 같은 상품이 색상별로
         * 여러 번 추천되지 않도록 사용
         */
        Set<Long> addedProductIds =
                new HashSet<>();


        /*
         * =====================================
         * 상품 검사
         * =====================================
         */
        for (
                Product product :
                products
        ) {

            if (
                    result.size() >=
                    limit
            ) {

                break;
            }


            /*
             * MAKEUP 전체 추천일 때는
             * 메이크업 상품만 허용
             */
            if (
                    "MAKEUP".equalsIgnoreCase(
                            productType
                    )
                    &&
                    !isMakeupProduct(
                            product
                    )
            ) {

                continue;
            }


            /*
             * 상품의 노출 가능한 컬러 조회
             */
            List<ProductColor> colors =
                    productColorRepository
                            .findByProductIdAndVisibleOrderBySortOrderAsc(
                                    product.getId(),
                                    "Y"
                            );


            for (
                    ProductColor productColor :
                    colors
            ) {

                if (
                        result.size() >=
                        limit
                ) {

                    break;
                }


                /*
                 * 이미 추천한 상품이면
                 * 다른 색상은 추가하지 않는다.
                 */
                if (
                        addedProductIds.contains(
                                product.getId()
                        )
                ) {

                    break;
                }


                /*
                 * 색상 마스터 없는 경우
                 */
                if (
                        productColor.getColorMaster()
                        == null
                ) {

                    continue;
                }


                String colorName =
                        productColor
                                .getColorMaster()
                                .getColorName();


                String baseColorGroup =
                        productColor
                                .getColorMaster()
                                .getBaseColorGroup();


                /*
                 * 퍼스널컬러와
                 * 상품 색상이 맞는지 검사
                 */
                if (
                        !matchesPersonalColor(
                                personalColor,
                                colorName,
                                baseColorGroup
                        )
                ) {

                    continue;
                }


                /*
                 * 대표 이미지
                 */
                String imageUrl =
                        findProductImageUrl(
                                productColor
                        );


                ChatbotRecommendedProductDto dto =
                        ChatbotRecommendedProductDto
                                .builder()

                                .productId(
                                        product.getId()
                                )

                                .productColorId(
                                        productColor.getId()
                                )

                                .productName(
                                        product.getName()
                                )

                                .brand(
                                        product.getBrand()
                                )

                                .price(
                                        product.getPrice()
                                )

                                .discount(
                                        product.getDiscount()
                                )

                                .colorName(
                                        colorName
                                )

                                .colorCode(
                                        productColor
                                                .getColorMaster()
                                                .getColorCode()
                                )

                                .imageUrl(
                                        imageUrl
                                )

                                .build();


                result.add(
                        dto
                );


                addedProductIds.add(
                        product.getId()
                );
            }
        }


        return result;
    }


    /*
     * =========================================
     * 메이크업 상품 여부
     * =========================================
     */
    private boolean isMakeupProduct(
            Product product
    ) {

        if (
                product == null
                ||
                product.getProductType()
                == null
        ) {

            return false;
        }


        String type =
                product
                        .getProductType()
                        .trim()
                        .toUpperCase();


        return (
                "LIP".equals(type)
                ||
                "BASE".equals(type)
                ||
                "CHEEK".equals(type)
                ||
                "EYE".equals(type)
        );
    }


    /*
     * =========================================
     * 퍼스널컬러별 색상 매칭
     *
     * PRODUCT_COLOR_MASTER의
     * colorName / baseColorGroup을 사용한다.
     * =========================================
     */
    private boolean matchesPersonalColor(
            String personalColor,
            String colorName,
            String baseColorGroup
    ) {

        if (
                personalColor == null
                ||
                personalColor.isBlank()
        ) {

            return false;
        }


        String text =
                safeLower(
                        colorName
                )
                +
                " "
                +
                safeLower(
                        baseColorGroup
                );


        String type =
                personalColor
                        .trim()
                        .toUpperCase();


        /*
         * =====================================
         * 봄 웜톤
         * =====================================
         */
        if (
                "SPRING_WARM".equals(
                        type
                )
        ) {

            return containsAny(
                    text,

                    "코랄",
                    "피치",
                    "살구",
                    "오렌지",

                    "웜핑크",
                    "웜 핑크",

                    "베이지",

                    "골드",
                    "골든",

                    "라이트브라운",
                    "라이트 브라운"
            );
        }


        /*
         * =====================================
         * 여름 쿨톤
         * =====================================
         */
        if (
                "SUMMER_COOL".equals(
                        type
                )
        ) {

            return containsAny(
                    text,

                    "로즈",

                    "라벤더",

                    "모브",

                    "쿨핑크",
                    "쿨 핑크",

                    "핑크",

                    "베리",

                    "라일락",

                    "소프트핑크",
                    "소프트 핑크"
            );
        }


        /*
         * =====================================
         * 가을 웜톤
         * =====================================
         */
        if (
                "AUTUMN_WARM".equals(
                        type
                )
        ) {

            return containsAny(
                    text,

                    "브릭",

                    "테라코타",

                    "칠리",

                    "브라운",

                    "카멜",

                    "코퍼",

                    "올리브",

                    "베이지",

                    "누드",

                    "웜브라운",
                    "웜 브라운"
            );
        }


        /*
         * =====================================
         * 겨울 쿨톤
         * =====================================
         */
        if (
                "WINTER_COOL".equals(
                        type
                )
        ) {

            return containsAny(
                    text,

                    "푸시아",

                    "와인",

                    "플럼",

                    "버건디",

                    "체리",

                    "딥로즈",
                    "딥 로즈",

                    "레드",

                    "베리",

                    "마젠타"
            );
        }


        return false;
    }


    /*
     * =========================================
     * 상품 대표 이미지
     *
     * 중요:
     *
     * 새로운 /uploads/product/... URL을
     * 임의로 만드는 것이 아니라
     *
     * PRODUCT_COLOR_IMAGE에 이미 저장된
     * filePath를 그대로 사용한다.
     * =========================================
     */
    private String findProductImageUrl(
            ProductColor productColor
    ) {

        /*
         * 1순위
         * 대표 이미지
         */
        ProductColorImage image =
                productColorImageRepository
                        .findFirstByProductColorIdAndIsMainOrderBySortOrderAsc(
                                productColor.getId(),
                                "Y"
                        )
                        .orElseGet(
                                () ->
                                        productColorImageRepository
                                                .findByProductColorIdOrderBySortOrderAsc(
                                                        productColor.getId()
                                                )
                                                .stream()
                                                .findFirst()
                                                .orElse(
                                                        null
                                                )
                        );


        /*
         * 이미지 없는 상품
         */
        if (
                image == null
        ) {

            return null;
        }


        /*
         * 기존 상품 시스템에서 사용하는
         * 실제 filePath 그대로 반환
         */
        return image.getFilePath();
    }


    /*
     * =========================================
     * null-safe lowercase
     * =========================================
     */
    private String safeLower(
            String value
    ) {

        if (
                value == null
        ) {

            return "";
        }


        return value
                .trim()
                .toLowerCase();
    }


    /*
     * =========================================
     * 키워드 포함 여부
     * =========================================
     */
    private boolean containsAny(
            String text,
            String... keywords
    ) {

        for (
                String keyword :
                keywords
        ) {

            if (
                    text.contains(
                            keyword.toLowerCase()
                    )
            ) {

                return true;
            }
        }


        return false;
    }
}