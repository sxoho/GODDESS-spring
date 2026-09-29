package dev.jpa.team1.beauty.tryon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.jpa.team1.beauty.tryonitem.BeautyTryonItem;
import dev.jpa.team1.beauty.tryonitem.BeautyTryonItemRepository;

import dev.jpa.team1.beauty.tryonresult.BeautyTryonResult;
import dev.jpa.team1.beauty.tryonresult.BeautyTryonResultRepository;

import dev.jpa.team1.member.memberphoto.MemberPhoto;
import dev.jpa.team1.member.memberphoto.MemberPhotoRepository;

import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;

import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productcolor.ProductColorRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.Duration;

import java.util.Base64;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BeautyTryonService {


    private final MemberPhotoRepository memberPhotoRepository;

    private final BeautyTryonRepository beautyTryonRepository;

    private final BeautyTryonItemRepository beautyTryonItemRepository;

    private final BeautyTryonResultRepository beautyTryonResultRepository;

    private final ProductRepository productRepository;

    private final ProductColorRepository productColorRepository;

    private final ObjectMapper objectMapper;


    /**
     * H200 AI BEAUTY FastAPI
     */
    @Value(
        "${BEAUTY_AI_BASE_URL:http://139.150.91.194:11502}"
    )
    private String beautyAiBaseUrl;


    /**
     * 회원사진 실제 저장 위치
     */
    private static final String MEMBER_PHOTO_UPLOAD_DIR =
            "uploads/member/face/";


    /**
     * AI BEAUTY 결과 이미지 저장 위치
     */
    private static final String BEAUTY_RESULT_UPLOAD_DIR =
            "uploads/beauty/tryon/";


    /**
     * 브라우저에서 접근할 결과 이미지 경로
     */
    private static final String BEAUTY_RESULT_WEB_PATH =
            "/images/beauty/tryon/";


    /**
     * AI BEAUTY 적용
     *
     * React
     * → Spring
     * → H200 FastAPI
     * → LangGraph
     * → OpenAI
     * → 결과 이미지
     * → BEAUTY_TRYON 저장
     * → BEAUTY_TRYON_ITEM 저장
     * → BEAUTY_TRYON_RESULT 저장
     */
    @Transactional
    public BeautyApplyResponseDto applyBeauty(
            BeautyApplyRequestDto requestDto
    ) {


        /* =========================================
           1. 기본 검증
        ========================================= */

        if (
            requestDto.getMemberId() == null
        ) {

            throw new IllegalArgumentException(
                "회원 ID가 필요합니다."
            );
        }


        boolean hasSelection =
                requestDto.getBase() != null
                ||
                requestDto.getLip() != null
                ||
                requestDto.getCheek() != null
                ||
                requestDto.getEye() != null
                ||
                hasText(
                    requestDto.getHairStyle()
                )
                ||
                hasText(
                    requestDto.getHairColor()
                );


        if (!hasSelection) {

            throw new IllegalArgumentException(
                "적용할 AI BEAUTY 항목을 하나 이상 선택해주세요."
            );
        }


        /* =========================================
           2. 회원 대표사진 조회
        ========================================= */

        MemberPhoto memberPhoto =
                memberPhotoRepository
                    .findFirstByMemberIdAndIsMainAndVisibleOrderByCreatedAtDesc(
                        requestDto.getMemberId(),
                        "Y",
                        "Y"
                    )
                    .orElseThrow(
                        () ->
                            new IllegalArgumentException(
                                "AI BEAUTY에 사용할 회원 대표사진이 없습니다."
                            )
                    );


        /* =========================================
           3. 회원사진 실제 파일 조회
        ========================================= */

        Path uploadDirectory =
                Paths
                    .get(
                        MEMBER_PHOTO_UPLOAD_DIR
                    )
                    .toAbsolutePath()
                    .normalize();


        Path imagePath =
                uploadDirectory
                    .resolve(
                        memberPhoto.getFileSaved()
                    )
                    .normalize();


        if (
            !imagePath.startsWith(
                uploadDirectory
            )
        ) {

            throw new IllegalStateException(
                "올바르지 않은 회원 사진 경로입니다."
            );
        }


        if (
            !Files.exists(
                imagePath
            )
        ) {

            throw new IllegalStateException(
                "회원 대표사진 파일을 찾을 수 없습니다: "
                +
                imagePath
            );
        }


        try {


            /* =========================================
               4. 회원사진 Base64
            ========================================= */

            String imageBase64 =
                    Base64
                        .getEncoder()
                        .encodeToString(
                            Files.readAllBytes(
                                imagePath
                            )
                        );


            String contentType =
                    Files.probeContentType(
                        imagePath
                    );


            if (
                contentType == null
                ||
                contentType.isBlank()
            ) {

                contentType =
                        "image/png";
            }


            /* =========================================
               5. FastAPI 요청 JSON
            ========================================= */

            ObjectNode requestJson =
                    objectMapper
                        .createObjectNode();


            requestJson.put(
                "image_base64",
                imageBase64
            );


            requestJson.put(
                "content_type",
                contentType
            );


            requestJson.put(
                "file_name",
                memberPhoto.getFileOrigin()
            );


            putProductSelection(
                requestJson,
                "base",
                requestDto.getBase()
            );


            putProductSelection(
                requestJson,
                "lip",
                requestDto.getLip()
            );


            putProductSelection(
                requestJson,
                "cheek",
                requestDto.getCheek()
            );


            putProductSelection(
                requestJson,
                "eye",
                requestDto.getEye()
            );


            if (
                hasText(
                    requestDto.getHairStyle()
                )
            ) {

                requestJson.put(
                    "hair_style",
                    requestDto
                        .getHairStyle()
                        .trim()
                );

            } else {

                requestJson.putNull(
                    "hair_style"
                );
            }


            if (
                hasText(
                    requestDto.getHairColor()
                )
            ) {

                requestJson.put(
                    "hair_color",
                    requestDto
                        .getHairColor()
                        .trim()
                );

            } else {

                requestJson.putNull(
                    "hair_color"
                );
            }


            /* =========================================
               6. FastAPI 호출
            ========================================= */

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


            HttpRequest request =
                    HttpRequest
                        .newBuilder()
                        .uri(
                            URI.create(
                                beautyAiBaseUrl
                                +
                                "/beauty/tryon"
                            )
                        )
                        .timeout(
                            Duration.ofSeconds(
                                180
                            )
                        )
                        .header(
                            "Content-Type",
                            "application/json"
                        )
                        .header(
                            "Accept",
                            "application/json"
                        )
                        .POST(
                            HttpRequest
                                .BodyPublishers
                                .ofString(
                                    requestJson.toString()
                                )
                        )
                        .build();


            HttpResponse<String> response =
                    client.send(
                        request,
                        HttpResponse
                            .BodyHandlers
                            .ofString()
                    );


            String responseBody =
                    response.body();


            if (
                response.statusCode() < 200
                ||
                response.statusCode() >= 300
            ) {

                throw new IllegalStateException(
                    "AI BEAUTY FastAPI 응답 오류: "
                    +
                    response.statusCode()
                    +
                    " / "
                    +
                    responseBody
                );
            }


            if (
                responseBody == null
                ||
                responseBody.isBlank()
            ) {

                throw new IllegalStateException(
                    "AI BEAUTY 결과가 없습니다."
                );
            }


            /* =========================================
               7. FastAPI 결과 파싱
            ========================================= */

            JsonNode root =
                    objectMapper
                        .readTree(
                            responseBody
                        );


            boolean success =
                    root
                        .path(
                            "success"
                        )
                        .asBoolean(
                            false
                        );


            String route =
                    root
                        .path(
                            "route"
                        )
                        .asText(
                            ""
                        );


            String status =
                    root
                        .path(
                            "status"
                        )
                        .asText(
                            ""
                        );


            String resultImageBase64 =
                    root
                        .path(
                            "result_image_base64"
                        )
                        .asText(
                            ""
                        );


            String resultContentType =
                    root
                        .path(
                            "result_content_type"
                        )
                        .asText(
                            "image/png"
                        );


            String resultFileName =
                    root
                        .path(
                            "result_file_name"
                        )
                        .asText(
                            ""
                        );


            if (
                !success
                ||
                resultImageBase64.isBlank()
            ) {

                throw new IllegalStateException(
                    "AI BEAUTY 결과 이미지가 생성되지 않았습니다."
                );
            }


            /* =========================================
               8. BEAUTY_TRYON 저장
        ========================================= */

            String beautyType =
                    resolveBeautyType(
                        requestDto
                    );


            BeautyTryon beautyTryon =
                    BeautyTryon
                        .builder()
                        .member(
                            memberPhoto.getMember()
                        )
                        .memberPhoto(
                            memberPhoto
                        )
                        .beautyType(
                            beautyType
                        )
                        .status(
                            "COMPLETED"
                        )
                        .build();


            beautyTryon =
                    beautyTryonRepository
                        .save(
                            beautyTryon
                        );


            /* =========================================
               9. BEAUTY_TRYON_ITEM 저장
            ========================================= */

            int sortOrder = 1;


            sortOrder =
                    saveProductItem(
                        beautyTryon,
                        "BASE",
                        requestDto.getBase(),
                        sortOrder
                    );


            sortOrder =
                    saveProductItem(
                        beautyTryon,
                        "LIP",
                        requestDto.getLip(),
                        sortOrder
                    );


            sortOrder =
                    saveProductItem(
                        beautyTryon,
                        "CHEEK",
                        requestDto.getCheek(),
                        sortOrder
                    );


            sortOrder =
                    saveProductItem(
                        beautyTryon,
                        "EYE",
                        requestDto.getEye(),
                        sortOrder
                    );


            if (
                hasText(
                    requestDto.getHairStyle()
                )
            ) {

                BeautyTryonItem hairStyleItem =
                        BeautyTryonItem
                            .builder()
                            .beautyTryon(
                                beautyTryon
                            )
                            .itemType(
                                "HAIR_STYLE"
                            )
                            .styleValue(
                                requestDto
                                    .getHairStyle()
                                    .trim()
                            )
                            .sortOrder(
                                sortOrder++
                            )
                            .build();


                beautyTryonItemRepository
                    .save(
                        hairStyleItem
                    );
            }


            if (
                hasText(
                    requestDto.getHairColor()
                )
            ) {

                BeautyTryonItem hairColorItem =
                        BeautyTryonItem
                            .builder()
                            .beautyTryon(
                                beautyTryon
                            )
                            .itemType(
                                "HAIR_COLOR"
                            )
                            .styleValue(
                                requestDto
                                    .getHairColor()
                                    .trim()
                            )
                            .sortOrder(
                                sortOrder++
                            )
                            .build();


                beautyTryonItemRepository
                    .save(
                        hairColorItem
                    );
            }


            /* =========================================
               10. 결과 이미지 파일 저장
            ========================================= */

            byte[] resultBytes =
                    Base64
                        .getDecoder()
                        .decode(
                            resultImageBase64
                        );


            Path resultDirectory =
                    Paths
                        .get(
                            BEAUTY_RESULT_UPLOAD_DIR
                        )
                        .toAbsolutePath()
                        .normalize();


            Files.createDirectories(
                resultDirectory
            );


            String extension =
                    resolveExtension(
                        resultContentType
                    );


            String savedFileName =
                    "beauty_"
                    +
                    UUID
                        .randomUUID()
                        .toString()
                        .replace(
                            "-",
                            ""
                        )
                    +
                    extension;


            Path resultPath =
                    resultDirectory
                        .resolve(
                            savedFileName
                        )
                        .normalize();


            if (
                !resultPath.startsWith(
                    resultDirectory
                )
            ) {

                throw new IllegalStateException(
                    "올바르지 않은 AI BEAUTY 결과 저장 경로입니다."
                );
            }


            Files.write(
                resultPath,
                resultBytes
            );


            String webFilePath =
                    BEAUTY_RESULT_WEB_PATH
                    +
                    savedFileName;


            /* =========================================
               11. BEAUTY_TRYON_RESULT 저장
            ========================================= */

            BeautyTryonResult beautyTryonResult =
                    BeautyTryonResult
                        .builder()
                        .beautyTryon(
                            beautyTryon
                        )
                        .fileOrigin(
                            hasText(
                                resultFileName
                            )
                                ? resultFileName
                                : savedFileName
                        )
                        .fileSaved(
                            savedFileName
                        )
                        .filePath(
                            webFilePath
                        )
                        .aiDescription(
                            buildAiDescription(
                                route,
                                status
                            )
                        )
                        .success(
                            "Y"
                        )
                        .build();


            beautyTryonResult =
                    beautyTryonResultRepository
                        .save(
                            beautyTryonResult
                        );


            /* =========================================
               12. React 응답
            ========================================= */

            return new BeautyApplyResponseDto(
                true,
                route,
                status,
                resultImageBase64,
                resultContentType,
                resultFileName,
                beautyTryon.getId(),
                beautyTryonResult.getId()
            );


        } catch (
            InterruptedException e
        ) {

            Thread
                .currentThread()
                .interrupt();


            throw new IllegalStateException(
                "AI BEAUTY 요청이 중단되었습니다.",
                e
            );


        } catch (
            Exception e
        ) {

            if (
                e instanceof IllegalArgumentException
            ) {

                throw (
                    IllegalArgumentException
                ) e;
            }


            if (
                e instanceof IllegalStateException
            ) {

                throw (
                    IllegalStateException
                ) e;
            }


            throw new IllegalStateException(
                "AI BEAUTY 처리 중 오류가 발생했습니다.",
                e
            );
        }
    }


    /* =========================================
       실제 상품 선택 ITEM 저장
    ========================================= */

    private int saveProductItem(
            BeautyTryon beautyTryon,
            String itemType,
            BeautyApplyRequestDto.ProductSelection selection,
            int sortOrder
    ) {


        if (
            selection == null
        ) {

            return sortOrder;
        }


        if (
            selection.getProductId() == null
        ) {

            throw new IllegalArgumentException(
                itemType
                +
                " 상품 ID가 없습니다."
            );
        }


        Product product =
                productRepository
                    .findById(
                        selection.getProductId()
                    )
                    .orElseThrow(
                        () ->
                            new IllegalArgumentException(
                                "상품을 찾을 수 없습니다. productId="
                                +
                                selection.getProductId()
                            )
                    );


        ProductColor productColor =
                null;


        if (
            selection.getColorId() != null
        ) {

            productColor =
                    productColorRepository
                        .findById(
                            selection.getColorId()
                        )
                        .orElseThrow(
                            () ->
                                new IllegalArgumentException(
                                    "상품 색상을 찾을 수 없습니다. productColorId="
                                    +
                                    selection.getColorId()
                                )
                        );


            if (
                productColor
                    .getProduct()
                    .getId()
                    .longValue()
                !=
                product
                    .getId()
                    .longValue()
            ) {

                throw new IllegalArgumentException(
                    "선택한 상품과 색상 정보가 일치하지 않습니다."
                );
            }
        }


        BeautyTryonItem item =
                BeautyTryonItem
                    .builder()
                    .beautyTryon(
                        beautyTryon
                    )
                    .product(
                        product
                    )
                    .productColor(
                        productColor
                    )
                    .itemType(
                        itemType
                    )
                    .sortOrder(
                        sortOrder
                    )
                    .build();


        beautyTryonItemRepository
            .save(
                item
            );


        return sortOrder + 1;
    }


    /* =========================================
       DB 제약조건에 맞는 BEAUTY_TYPE 판별
       
       허용 값:
       MAKEUP
       HAIR_STYLE
       HAIR_COLOR
    ========================================= */

    private String resolveBeautyType(
            BeautyApplyRequestDto requestDto
    ) {


        boolean hasMakeup =
                requestDto.getBase() != null
                ||
                requestDto.getLip() != null
                ||
                requestDto.getCheek() != null
                ||
                requestDto.getEye() != null;


        boolean hasHairStyle =
                hasText(
                    requestDto.getHairStyle()
                );


        boolean hasHairColor =
                hasText(
                    requestDto.getHairColor()
                );


        /*
         * 메이크업 상품이 하나라도 선택되어 있으면
         * 대표 BEAUTY_TYPE은 MAKEUP으로 저장.
         *
         * 헤어가 함께 적용된 경우에도
         * 실제 헤어 선택 정보는 BEAUTY_TRYON_ITEM에
         * 별도로 저장된다.
         */
        if (hasMakeup) {

            return "MAKEUP";
        }


        /*
         * 메이크업 없이 헤어스타일을 선택한 경우
         */
        if (hasHairStyle) {

            return "HAIR_STYLE";
        }


        /*
         * 헤어컬러만 선택한 경우
         */
        if (hasHairColor) {

            return "HAIR_COLOR";
        }


        throw new IllegalArgumentException(
            "AI BEAUTY 적용 항목이 없습니다."
        );
    }


    /* =========================================
       AI 설명
    ========================================= */

    private String buildAiDescription(
            String route,
            String status
    ) {


        String safeRoute =
                nullSafe(
                    route
                );


        String safeStatus =
                nullSafe(
                    status
                );


        return "route="
                +
                safeRoute
                +
                " / status="
                +
                safeStatus;
    }


    /* =========================================
       이미지 확장자
    ========================================= */

    private String resolveExtension(
            String contentType
    ) {


        if (
            contentType == null
        ) {

            return ".png";
        }


        String lower =
                contentType
                    .toLowerCase();


        if (
            lower.contains(
                "jpeg"
            )
            ||
            lower.contains(
                "jpg"
            )
        ) {

            return ".jpg";
        }


        if (
            lower.contains(
                "webp"
            )
        ) {

            return ".webp";
        }


        return ".png";
    }


    /* =========================================
       FastAPI 상품 선택 JSON 생성
    ========================================= */

    private void putProductSelection(
            ObjectNode root,
            String fieldName,
            BeautyApplyRequestDto.ProductSelection selection
    ) {


        if (
            selection == null
        ) {

            root.putNull(
                fieldName
            );

            return;
        }


        ObjectNode node =
                objectMapper
                    .createObjectNode();


        if (
            selection.getProductId() != null
        ) {

            node.put(
                "product_id",
                selection.getProductId()
            );
        }


        node.put(
            "product_name",
            nullSafe(
                selection.getProductName()
            )
        );


        node.put(
            "brand",
            nullSafe(
                selection.getBrand()
            )
        );


        if (
            selection.getColorId() != null
        ) {

            node.put(
                "color_id",
                selection.getColorId()
            );
        }


        node.put(
            "color_name",
            nullSafe(
                selection.getColorName()
            )
        );


        node.put(
            "color_code",
            nullSafe(
                selection.getColorCode()
            )
        );


        root.set(
            fieldName,
            node
        );
    }


    /* =========================================
       문자열 존재 여부
    ========================================= */

    private boolean hasText(
            String value
    ) {

        return value != null
                &&
                !value
                    .trim()
                    .isEmpty();
    }


    /* =========================================
       null → ""
    ========================================= */

    private String nullSafe(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }
}