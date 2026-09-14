package dev.jpa.team1.member.memberphoto;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.time.Duration;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MemberPhotoService {

    private final MemberPhotoRepository memberPhotoRepository;

    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper;


    /**
     * 회원 사진 AI FastAPI 주소
     *
     * Team 5
     * H200 회원 사진 검증 FastAPI
     *
     * Port:
     * 11501
     *
     * 현재:
     * http://139.150.91.194:11501
     */
    @Value("${MEMBER_PHOTO_AI_BASE_URL:http://139.150.91.194:11501}")
    private String memberPhotoAiBaseUrl;


    /**
     * 회원 사진 저장 폴더
     */
    private static final String UPLOAD_DIR =
            "uploads/member/face/";


    /**
     * 허용 이미지 확장자
     */
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    ".jpg",
                    ".jpeg",
                    ".png"
            );


    /**
     * 최대 파일 크기
     *
     * 10MB
     */
    private static final long MAX_FILE_SIZE =
            10L * 1024L * 1024L;


    /**
     * 회원 대표 사진 조회
     */
    @Transactional(readOnly = true)
    public Optional<MemberPhotoResponseDto> findMainPhoto(
            Long memberId
    ) {

        return memberPhotoRepository
                .findFirstByMemberIdAndIsMainAndVisibleOrderByCreatedAtDesc(
                        memberId,
                        "Y",
                        "Y"
                )
                .map(
                        MemberPhotoResponseDto::from
                );
    }


    /**
     * 회원 전체 사진 조회
     *
     * 현재 화면에서는 대표 사진 1장만 사용하지만
     * 추후 여러 사진 기능 확장을 위해 유지한다.
     */
    @Transactional(readOnly = true)
    public List<MemberPhotoResponseDto> findByMemberId(
            Long memberId
    ) {

        return memberPhotoRepository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        MemberPhotoResponseDto::from
                )
                .toList();
    }


    /**
     * 회원 사진 AI 검사
     *
     * 사진 자체는 저장하지 않는다.
     *
     * Spring
     * → H200 FastAPI 11501
     *
     * 검사 항목:
     *
     * 1. 얼굴 검출 여부
     * 2. 정면 얼굴 여부
     * 3. 얼굴 중심 편차
     * 4. 밝기
     * 5. 너무 어두운지 여부
     * 6. 너무 밝은지 여부
     * 7. 선명도
     * 8. 흐림 여부
     * 9. 최종 사용 가능 여부
     */
    public MemberPhotoAiCheckDto checkPhoto(
            MultipartFile file
    ) {

        /**
         * 기본 파일 검사
         */
        validateFile(
                file
        );


        try {

            /**
             * 이미지 MIME 타입
             */
            String contentType =
                    file.getContentType();


            if (
                    contentType == null ||
                    contentType.isBlank()
            ) {

                contentType =
                        "application/octet-stream";
            }


            /**
             * 이미지 파일
             * → Base64 문자열
             */
            String imageBase64 =
                    Base64
                            .getEncoder()
                            .encodeToString(
                                    file.getBytes()
                            );


            /**
             * FastAPI 요청 JSON
             *
             * {
             *   "image_base64": "...",
             *   "content_type": "image/jpeg",
             *   "file_name": "face.jpg"
             * }
             */
            String requestBody =
                    objectMapper
                            .createObjectNode()
                            .put(
                                    "image_base64",
                                    imageBase64
                            )
                            .put(
                                    "content_type",
                                    contentType
                            )
                            .put(
                                    "file_name",
                                    file.getOriginalFilename()
                            )
                            .toString();


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
             * 회원 사진 AI 검증 요청
             *
             * POST
             *
             * http://139.150.91.194:11501/member_photo_check
             */
            HttpRequest request =
                    HttpRequest
                            .newBuilder()
                            .uri(
                                    URI.create(
                                            memberPhotoAiBaseUrl
                                                    + "/member_photo_check"
                                    )
                            )
                            .timeout(
                                    Duration.ofSeconds(
                                            30
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
                                                    requestBody
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
             * FastAPI HTTP 오류
             */
            if (
                    response.statusCode() < 200 ||
                    response.statusCode() >= 300
            ) {

                throw new IllegalStateException(
                        "FastAPI 응답 오류: "
                                + response.statusCode()
                                + " / "
                                + response.body()
                );
            }


            String responseBody =
                    response.body();


            /**
             * 응답 데이터 없음
             */
            if (
                    responseBody == null ||
                    responseBody.isBlank()
            ) {

                throw new IllegalStateException(
                        "사진 AI 검사 결과가 없습니다."
                );
            }


            /**
             * FastAPI 응답 예시
             *
             * {
             *   "face_detected": true,
             *   "is_front": true,
             *   "face_center_offset": 0.0218,
             *   "brightness": 184.16,
             *   "is_too_dark": false,
             *   "is_too_bright": false,
             *   "blur_score": 90.96,
             *   "is_blurred": false,
             *   "valid": true,
             *   "message":
             *   "AI 뷰티 체험에 사용할 수 있는 정상적인 얼굴 사진입니다."
             * }
             */
            JsonNode root =
                    objectMapper
                            .readTree(
                                    responseBody
                            );


            /**
             * 얼굴 검출 여부
             */
            boolean faceDetected =
                    root
                            .path(
                                    "face_detected"
                            )
                            .asBoolean(
                                    false
                            );


            /**
             * 정면 얼굴 여부
             */
            boolean isFront =
                    root
                            .path(
                                    "is_front"
                            )
                            .asBoolean(
                                    false
                            );


            /**
             * 얼굴 중심 편차
             *
             * 얼굴 검출 실패 시 null 가능
             */
            Double faceCenterOffset =
                    root.hasNonNull(
                            "face_center_offset"
                    )
                            ? root
                                    .get(
                                            "face_center_offset"
                                    )
                                    .asDouble()
                            : null;


            /**
             * 이미지 평균 밝기
             *
             * 검사할 수 없는 경우 null 가능
             */
            Double brightness =
                    root.hasNonNull(
                            "brightness"
                    )
                            ? root
                                    .get(
                                            "brightness"
                                    )
                                    .asDouble()
                            : null;


            /**
             * 너무 어두운 사진 여부
             */
            boolean isTooDark =
                    root
                            .path(
                                    "is_too_dark"
                            )
                            .asBoolean(
                                    false
                            );


            /**
             * 너무 밝은 사진 여부
             */
            boolean isTooBright =
                    root
                            .path(
                                    "is_too_bright"
                            )
                            .asBoolean(
                                    false
                            );


            /**
             * 선명도 점수
             *
             * Laplacian 분산값
             */
            Double blurScore =
                    root.hasNonNull(
                            "blur_score"
                    )
                            ? root
                                    .get(
                                            "blur_score"
                                    )
                                    .asDouble()
                            : null;


            /**
             * 흐림 여부
             */
            boolean isBlurred =
                    root
                            .path(
                                    "is_blurred"
                            )
                            .asBoolean(
                                    false
                            );


            /**
             * AI BEAUTY 회원사진으로
             * 최종 사용 가능한지 여부
             */
            boolean valid =
                    root
                            .path(
                                    "valid"
                            )
                            .asBoolean(
                                    false
                            );


            /**
             * 사용자 안내 메시지
             */
            String message =
                    root
                            .path(
                                    "message"
                            )
                            .asText(
                                    "사진 검사 결과를 확인할 수 없습니다."
                            );


            /**
             * 검사 결과 반환
             */
            return new MemberPhotoAiCheckDto(
                    faceDetected,
                    isFront,
                    faceCenterOffset,
                    brightness,
                    isTooDark,
                    isTooBright,
                    blurScore,
                    isBlurred,
                    valid,
                    message
            );


        } catch (Exception e) {

            System.out.println(
                    "-> 회원 사진 AI 검사 오류: "
                            + e.getMessage()
            );


            throw new IllegalStateException(
                    "회원 사진 AI 검사 중 오류가 발생했습니다.",
                    e
            );
        }
    }


    /**
     * 회원 사진 등록 / 변경
     *
     * 처리 순서:
     *
     * 1. 회원 조회
     * 2. 파일 기본 검사
     * 3. FastAPI 11501 AI 검사
     * 4. 부적합 사진이면 등록 중단
     * 5. 기존 대표 사진 해제
     * 6. 새 사진 파일 저장
     * 7. DB 저장
     */
    @Transactional
    public MemberPhotoResponseDto upload(
            Long memberId,
            MultipartFile file
    ) throws IOException {

        /**
         * 1. 회원 조회
         */
        Member member =
                memberRepository
                        .findById(
                                memberId
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "회원 정보를 찾을 수 없습니다."
                                        )
                        );


        /**
         * 2. 파일 기본 검사
         */
        validateFile(
                file
        );


        /**
         * 3. 회원 사진 AI 검사
         *
         * H200 FastAPI
         * Port 11501
         *
         * 검사 항목:
         *
         * - 얼굴 검출
         * - 정면 여부
         * - 밝기
         * - 과도한 밝기
         * - 과도한 어두움
         * - 흐림 여부
         */
        MemberPhotoAiCheckDto aiResult =
                checkPhoto(
                        file
                );


        /**
         * 4. AI 검사 실패 시
         * 회원 사진을 저장하지 않는다.
         */
        if (
                !Boolean.TRUE.equals(
                        aiResult.getValid()
                )
        ) {

            String message =
                    aiResult.getMessage();


            if (
                    message == null ||
                    message.isBlank()
            ) {

                message =
                        "AI 뷰티 체험에 사용할 수 없는 사진입니다.";
            }


            throw new IllegalArgumentException(
                    message
            );
        }


        /**
         * 5. 기존 대표 사진 해제
         *
         * 중요:
         *
         * 새 사진이 AI 검사를 통과한 뒤에만
         * 기존 대표 사진을 해제한다.
         *
         * 따라서 새 사진 검사 실패 시
         * 기존 대표 사진은 그대로 유지된다.
         */
        Optional<MemberPhoto> currentMainPhoto =
                memberPhotoRepository
                        .findFirstByMemberIdAndIsMainAndVisibleOrderByCreatedAtDesc(
                                memberId,
                                "Y",
                                "Y"
                        );


        if (
                currentMainPhoto.isPresent()
        ) {

            currentMainPhoto
                    .get()
                    .removeMain();
        }


        /**
         * 6. 원본 파일명
         */
        String fileOrigin =
                file.getOriginalFilename();


        if (
                fileOrigin == null ||
                fileOrigin.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "파일명이 올바르지 않습니다."
            );
        }


        /**
         * 7. 확장자 추출
         */
        String extension =
                getExtension(
                        fileOrigin
                );


        /**
         * 8. 서버 저장 파일명 생성
         *
         * 예:
         *
         * member_7_a34e41ff.jpg
         */
        String uniqueId =
                UUID
                        .randomUUID()
                        .toString()
                        .substring(
                                0,
                                8
                        );


        String fileSaved =
                "member_"
                        + memberId
                        + "_"
                        + uniqueId
                        + extension;


        /**
         * 9. 저장 폴더 생성
         */
        Path uploadPath =
                Paths
                        .get(
                                UPLOAD_DIR
                        )
                        .toAbsolutePath()
                        .normalize();


        if (
                !Files.exists(
                        uploadPath
                )
        ) {

            Files.createDirectories(
                    uploadPath
            );
        }


        /**
         * 10. 최종 저장 경로
         */
        Path savePath =
                uploadPath
                        .resolve(
                                fileSaved
                        )
                        .normalize();


        /**
         * 저장 경로 이탈 방지
         */
        if (
                !savePath.startsWith(
                        uploadPath
                )
        ) {

            throw new IllegalArgumentException(
                    "올바르지 않은 파일 경로입니다."
            );
        }


        /**
         * 11. 실제 파일 저장
         */
        Files.copy(
                file.getInputStream(),
                savePath,
                StandardCopyOption.REPLACE_EXISTING
        );


        /**
         * 12. React에서 접근할 이미지 경로
         */
        String filePath =
                "/images/member/face/"
                        + fileSaved;


        /**
         * 13. MEMBER_PHOTO DB 저장
         *
         * 현재는 FACE 타입만 사용한다.
         */
        MemberPhoto memberPhoto =
                MemberPhoto
                        .builder()
                        .member(
                                member
                        )
                        .photoType(
                                "FACE"
                        )
                        .fileOrigin(
                                fileOrigin
                        )
                        .fileSaved(
                                fileSaved
                        )
                        .filePath(
                                filePath
                        )
                        .fileSize(
                                file.getSize()
                        )
                        .isMain(
                                "Y"
                        )
                        .visible(
                                "Y"
                        )
                        .build();


        MemberPhoto savedPhoto =
                memberPhotoRepository
                        .save(
                                memberPhoto
                        );


        return MemberPhotoResponseDto
                .from(
                        savedPhoto
                );
    }


    /**
     * 사진 사용 중지
     *
     * 실제 파일과 DB 행을 삭제하지 않고
     * visible = N 처리한다.
     */
    @Transactional
    public void hidePhoto(
            Long memberPhotoId
    ) {

        MemberPhoto memberPhoto =
                memberPhotoRepository
                        .findById(
                                memberPhotoId
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "회원 사진을 찾을 수 없습니다."
                                        )
                        );


        memberPhoto.hide();
    }


    /**
     * 파일 기본 검사
     */
    private void validateFile(
            MultipartFile file
    ) {

        /**
         * 파일 존재 여부
         */
        if (
                file == null ||
                file.isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "사진 파일을 선택해주세요."
            );
        }


        /**
         * 파일 크기 검사
         */
        if (
                file.getSize() >
                MAX_FILE_SIZE
        ) {

            throw new IllegalArgumentException(
                    "사진은 10MB 이하만 등록할 수 있습니다."
            );
        }


        /**
         * 원본 파일명 검사
         */
        String fileOrigin =
                file.getOriginalFilename();


        if (
                fileOrigin == null ||
                fileOrigin.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "파일명이 올바르지 않습니다."
            );
        }


        /**
         * 확장자 검사
         */
        String extension =
                getExtension(
                        fileOrigin
                );


        if (
                !ALLOWED_EXTENSIONS
                        .contains(
                                extension
                        )
        ) {

            throw new IllegalArgumentException(
                    "JPG, JPEG, PNG 파일만 등록할 수 있습니다."
            );
        }


        /**
         * MIME 타입 검사
         */
        String contentType =
                file.getContentType();


        if (
                contentType == null ||
                !contentType.startsWith(
                        "image/"
                )
        ) {

            throw new IllegalArgumentException(
                    "이미지 파일만 등록할 수 있습니다."
            );
        }
    }


    /**
     * 파일 확장자 추출
     */
    private String getExtension(
            String fileName
    ) {

        int dotIndex =
                fileName
                        .lastIndexOf(
                                "."
                        );


        if (
                dotIndex < 0
        ) {

            return "";
        }


        return fileName
                .substring(
                        dotIndex
                )
                .toLowerCase();
    }
}