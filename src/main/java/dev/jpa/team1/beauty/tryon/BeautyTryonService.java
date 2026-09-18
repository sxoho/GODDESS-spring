package dev.jpa.team1.beauty.tryon;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.jpa.team1.member.memberphoto.MemberPhoto;
import dev.jpa.team1.member.memberphoto.MemberPhotoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.Duration;

import java.util.Base64;


@Service
@RequiredArgsConstructor
public class BeautyTryonService {


    private final MemberPhotoRepository memberPhotoRepository;

    private final ObjectMapper objectMapper;


    /**
     * H200 AI BEAUTY FastAPI
     *
     * Team 5
     * Port 11502
     */
    @Value("${BEAUTY_AI_BASE_URL:http://139.150.91.194:11502}")
    private String beautyAiBaseUrl;


    /**
     * 회원 사진 실제 저장 폴더
     *
     * MemberPhotoService와 동일
     */
    private static final String MEMBER_PHOTO_UPLOAD_DIR =
            "uploads/member/face/";


    /**
     * AI BEAUTY 적용
     *
     * React
     * → Spring
     * → H200 FastAPI 11502
     * → LangGraph
     * → OpenAI Image Edit
     */
    public BeautyApplyResponseDto applyBeauty(
            BeautyApplyRequestDto requestDto
    ) {

        if (
                requestDto.getMemberId() == null
        ) {

            throw new IllegalArgumentException(
                    "회원 ID가 필요합니다."
            );
        }


        /**
         * 최소 한 개 이상 선택했는지 확인
         */
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


        /**
         * 회원 대표사진 조회
         */
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


        /**
         * 실제 저장된 이미지 파일 찾기
         *
         * DB filePath는:
         * /images/member/face/xxx.png
         *
         * 실제 저장 위치는:
         * uploads/member/face/xxx.png
         *
         * 따라서 fileSaved 기준으로 실제 파일을 찾는다.
         */
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


        /**
         * 저장 폴더 밖으로 나가는 경로 방지
         */
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
                            + imagePath
            );
        }


        try {

            /**
             * 이미지 파일 → Base64
             */
            String imageBase64 =
                    Base64
                            .getEncoder()
                            .encodeToString(
                                    Files.readAllBytes(
                                            imagePath
                                    )
                            );


            /**
             * MIME 타입
             */
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


            /**
             * FastAPI 요청 JSON 생성
             */
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


            /**
             * 메이크업 선택
             */
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


            /**
             * React 내부에서는 blush를 사용하더라도
             * Spring부터는 cheek로 통일
             */
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


            /**
             * 헤어
             */
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


            /**
             * Java HttpClient
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
             * OpenAI 이미지 생성은 시간이 조금 걸릴 수 있으므로
             * 요청 timeout을 넉넉하게 설정
             */
            HttpRequest request =
                    HttpRequest
                            .newBuilder()
                            .uri(
                                    URI.create(
                                            beautyAiBaseUrl
                                                    + "/beauty/tryon"
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


            String responseBody =
                    response.body();


            /**
             * FastAPI 오류
             */
            if (
                    response.statusCode() < 200
                            ||
                    response.statusCode() >= 300
            ) {

                throw new IllegalStateException(
                        "AI BEAUTY FastAPI 응답 오류: "
                                + response.statusCode()
                                + " / "
                                + responseBody
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


            /**
             * FastAPI 응답 JSON
             *
             * {
             *   "success": true,
             *   "route": "base",
             *   "status": "베이스 적용 완료",
             *   "result_image_base64": "...",
             *   "result_content_type": "image/png",
             *   "result_file_name": "..."
             * }
             */
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


            return new BeautyApplyResponseDto(
                    true,
                    route,
                    status,
                    resultImageBase64,
                    resultContentType,
                    resultFileName
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


    /**
     * FastAPI 상품 선택 JSON 생성
     *
     * Java camelCase
     * →
     * FastAPI snake_case
     */
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


    /**
     * 문자열 존재 여부
     */
    private boolean hasText(
            String value
    ) {

        return value != null
                &&
                !value.trim().isEmpty();
    }


    /**
     * null → ""
     */
    private String nullSafe(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }
}