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

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MemberPhotoService {

    private final MemberPhotoRepository memberPhotoRepository;

    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper;


    /**
     * 회원 사진 AI FastAPI 주소
     *
     * 기본:
     * http://localhost:11103
     */
    @Value("${MEMBER_PHOTO_AI_BASE_URL:http://localhost:11103}")
    private String memberPhotoAiBaseUrl;


    /**
     * 회원 사진 저장 폴더
     */
    private static final String UPLOAD_DIR =
            "uploads/member/face/";


    /**
     * 현재 허용하는 이미지 확장자
     */
    private static final Set<String>
            ALLOWED_EXTENSIONS =
            Set.of(
                    ".jpg",
                    ".jpeg",
                    ".png"
            );


    /**
     * 최대 파일 크기
     * 10MB
     */
    private static final long MAX_FILE_SIZE =
            10L * 1024L * 1024L;


    /**
     * 회원의 대표 사진 조회
     */
    @Transactional(readOnly = true)
    public Optional<MemberPhotoResponseDto>
    findMainPhoto(
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
     * 추후 여러 사진 기능 확장을 위해 유지
     */
    @Transactional(readOnly = true)
    public List<MemberPhotoResponseDto>
    findByMemberId(
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
     * 사진은 저장하지 않는다.
     *
     * React
     * → Spring
     * → FastAPI
     *
     * Spring에서는
     * 이미지를 Base64 문자열로 변환한 뒤
     * JSON 형식으로 FastAPI에 전달한다.
     */
    public MemberPhotoAiCheckDto checkPhoto(
            MultipartFile file
    ) {

        /**
         * 기존 사진 등록과 동일한
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
             * FastAPI로 보낼 JSON 생성
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
             * Java 기본 HttpClient
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
             * FastAPI 요청
             *
             * POST
             * http://localhost:11103/member_photo_check
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
             * FastAPI 오류 응답
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
             * 응답 없음
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
             * FastAPI 응답
             *
             * {
             *   "face_detected": true,
             *   "is_front": true,
             *   "message":
             *   "정면 얼굴 사진으로 확인되었습니다."
             * }
             */
            JsonNode root =
                    objectMapper
                            .readTree(
                                    responseBody
                            );


            boolean faceDetected =
                    root
                            .path(
                                    "face_detected"
                            )
                            .asBoolean(
                                    false
                            );


            boolean isFront =
                    root
                            .path(
                                    "is_front"
                            )
                            .asBoolean(
                                    false
                            );


            String message =
                    root
                            .path(
                                    "message"
                            )
                            .asText(
                                    "사진 검사 결과를 확인할 수 없습니다."
                            );


            return new MemberPhotoAiCheckDto(
                    faceDetected,
                    isFront,
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
     * 회원 사진 등록
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
         * 3. 기존 대표 사진 해제
         *
         * 현재 화면에서는 사진 1장만 사용하지만
         * 기존 사진은 DB에 남겨 추후 확장 가능하도록 함
         */
        Optional<MemberPhoto>
                currentMainPhoto =
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
         * 4. 원본 파일명
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
         * 5. 확장자 추출
         */
        String extension =
                getExtension(
                        fileOrigin
                );


        /**
         * 6. 서버 저장 파일명 생성
         *
         * 예:
         * member_7_a34e41ff.jpg
         */
        String uniqueId =
                UUID.randomUUID()
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
         * 7. 저장 폴더 생성
         */
        Path uploadPath =
                Paths.get(
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
         * 8. 최종 저장 경로
         */
        Path savePath =
                uploadPath
                        .resolve(
                                fileSaved
                        )
                        .normalize();


        /**
         * 경로 이탈 방지
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
         * 9. 실제 파일 저장
         */
        Files.copy(
                file.getInputStream(),
                savePath,
                StandardCopyOption.REPLACE_EXISTING
        );


        /**
         * 10. React에서 접근할 이미지 경로
         */
        String filePath =
                "/images/member/face/"
                        + fileSaved;


        /**
         * 11. DB 저장
         *
         * 현재는 FACE만 사용
         */
        MemberPhoto memberPhoto =
                MemberPhoto.builder()
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
     * visible = N 처리
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
     * 파일 검사
     */
    private void validateFile(
            MultipartFile file
    ) {

        if (
                file == null ||
                file.isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "사진 파일을 선택해주세요."
            );
        }


        if (
                file.getSize()
                        > MAX_FILE_SIZE
        ) {

            throw new IllegalArgumentException(
                    "사진은 10MB 이하만 등록할 수 있습니다."
            );
        }


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
         * MIME 타입 기본 검사
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
     * 확장자 추출
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