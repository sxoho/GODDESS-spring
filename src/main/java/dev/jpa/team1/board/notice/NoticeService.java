package dev.jpa.team1.board.notice;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final MemberRepository memberRepository;


    /**
     * 공지사항 첨부파일 저장 폴더
     */
    private static final String NOTICE_UPLOAD_DIR =
            "uploads/notice";


    /**
     * 관리자 공지사항 전체 목록
     */
    @Transactional(readOnly = true)
    public List<NoticeResponseDto>
    findAdminList() {

        return noticeRepository
                .findAllByOrderByFixedDescCreatedAtDesc()
                .stream()
                .map(
                        NoticeResponseDto::from
                )
                .toList();
    }


    /**
     * 일반 사용자 공개 공지사항 목록
     */
    @Transactional(readOnly = true)
    public List<NoticeResponseDto>
    findPublicList() {

        return noticeRepository
                .findByVisibleOrderByFixedDescCreatedAtDesc(
                        "Y"
                )
                .stream()
                .map(
                        NoticeResponseDto::from
                )
                .toList();
    }


    /**
     * 관리자 상세 조회
     *
     * 관리자 관리 화면에서는
     * 조회수를 증가시키지 않는다.
     */
    @Transactional(readOnly = true)
    public NoticeResponseDto
    findAdminDetail(
            Long noticeId
    ) {

        Notice notice =
                findNotice(
                        noticeId
                );


        return NoticeResponseDto.from(
                notice
        );
    }


    /**
     * 일반 사용자 상세 조회
     *
     * NOTICE 화면에서 공지를 열 때
     * 조회수 +1
     */
    @Transactional
    public NoticeResponseDto
    findPublicDetail(
            Long noticeId
    ) {

        Notice notice =
                findNotice(
                        noticeId
                );


        /**
         * 비공개 공지는 일반 화면에서 조회 불가
         */
        if (
                !"Y".equals(
                        notice.getVisible()
                )
        ) {

            throw new IllegalStateException(
                    "공개되지 않은 공지사항입니다."
            );
        }


        notice.increaseViewCount();


        return NoticeResponseDto.from(
                notice
        );
    }


    /**
     * 공지사항 등록
     */
    @Transactional
    public NoticeResponseDto save(
            NoticeRequestDto requestDto,
            MultipartFile file
    ) {

        Member admin =
                findAdmin(
                        requestDto.getAdminId()
                );


        /**
         * 작성자 표시 이름
         *
         * 입력하지 않은 경우 관리자
         */
        String writerName =
                normalizeWriterName(
                        requestDto.getWriterName()
                );


        Notice notice =
                Notice.builder()
                        .admin(
                                admin
                        )
                        .writerName(
                                writerName
                        )
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
                        .linkUrl(
                                normalizeOptionalText(
                                        requestDto
                                                .getLinkUrl()
                                )
                        )
                        .viewCount(
                                0
                        )
                        .fixed(
                                normalizeYn(
                                        requestDto
                                                .getFixed()
                                )
                        )
                        .visible(
                                normalizeYn(
                                        requestDto
                                                .getVisible()
                                )
                        )
                        .build();


        /**
         * 첨부파일이 있는 경우에만 저장
         */
        if (
                file != null &&
                !file.isEmpty()
        ) {

            saveFile(
                    notice,
                    file
            );
        }


        Notice savedNotice =
                noticeRepository.save(
                        notice
                );


        return NoticeResponseDto.from(
                savedNotice
        );
    }


    /**
     * 공지사항 수정
     */
    @Transactional
    public NoticeResponseDto update(
            Long noticeId,
            NoticeRequestDto requestDto,
            MultipartFile file
    ) {

        Notice notice =
                findNotice(
                        noticeId
                );


        /**
         * 수정 요청자 관리자 권한 확인
         */
        findAdmin(
                requestDto.getAdminId()
        );


        notice.update(

                normalizeWriterName(
                        requestDto
                                .getWriterName()
                ),

                requestDto
                        .getTitle()
                        .trim(),

                requestDto
                        .getContent()
                        .trim(),

                normalizeOptionalText(
                        requestDto
                                .getLinkUrl()
                ),

                normalizeYn(
                        requestDto
                                .getFixed()
                ),

                normalizeYn(
                        requestDto
                                .getVisible()
                )
        );


        /**
         * 새 파일을 선택한 경우
         *
         * 기존 파일 삭제 후
         * 새 첨부파일로 교체
         */
        if (
                file != null &&
                !file.isEmpty()
        ) {

            deletePhysicalFile(
                    notice
            );


            saveFile(
                    notice,
                    file
            );
        }


        return NoticeResponseDto.from(
                notice
        );
    }


    /**
     * 공지사항 삭제
     */
    @Transactional
    public void delete(
            Long noticeId,
            Long adminId
    ) {

        /**
         * 관리자 권한 확인
         */
        findAdmin(
                adminId
        );


        Notice notice =
                findNotice(
                        noticeId
                );


        /**
         * 첨부파일이 있다면
         * 실제 파일도 삭제
         */
        deletePhysicalFile(
                notice
        );


        noticeRepository.delete(
                notice
        );
    }


    /**
     * 관리자 조회 및 권한 확인
     */
    private Member findAdmin(
            Long adminId
    ) {

        Member admin =
                memberRepository
                        .findById(
                                adminId
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "관리자 회원 정보를 찾을 수 없습니다."
                                        )
                        );


        /**
         * 관리자 등급 확인
         *
         * GRADE_ID = 4
         */
        if (
                admin.getGrade() == null ||
                admin.getGrade().getId() == null ||
                admin.getGrade().getId() != 4L
        ) {

            throw new IllegalStateException(
                    "관리자만 공지사항을 관리할 수 있습니다."
            );
        }


        return admin;
    }


    /**
     * 공지 조회 공통
     */
    private Notice findNotice(
            Long noticeId
    ) {

        return noticeRepository
                .findById(
                        noticeId
                )
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "공지사항을 찾을 수 없습니다."
                                )
                );
    }


    /**
     * 첨부파일 저장
     */
    private void saveFile(
            Notice notice,
            MultipartFile file
    ) {

        try {

            /**
             * uploads/notice 폴더 생성
             */
            Path uploadPath =
                    Paths.get(
                            NOTICE_UPLOAD_DIR
                    )
                    .toAbsolutePath()
                    .normalize();


            Files.createDirectories(
                    uploadPath
            );


            /**
             * 사용자가 업로드한 원본 파일명
             */
            String originalFileName =
                    StringUtils.cleanPath(
                            file.getOriginalFilename() == null
                                    ? "file"
                                    : file.getOriginalFilename()
                    );


            /**
             * 확장자 추출
             */
            String extension =
                    StringUtils.getFilenameExtension(
                            originalFileName
                    );


            /**
             * 서버에 저장할 파일명
             *
             * 같은 이름의 파일이 올라와도
             * 덮어쓰지 않도록 UUID 사용
             */
            String savedFileName =
                    UUID.randomUUID()
                            .toString()
                            .replace(
                                    "-",
                                    ""
                            );


            if (
                    extension != null &&
                    !extension.isBlank()
            ) {

                savedFileName +=
                        "." + extension;
            }


            Path targetPath =
                    uploadPath.resolve(
                            savedFileName
                    );


            /**
             * 실제 파일 저장
             */
            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            /**
             * NOTICE 테이블에 저장할
             * 파일 정보 설정
             */
            notice.updateFile(

                    originalFileName,

                    savedFileName,

                    NOTICE_UPLOAD_DIR,

                    file.getSize()
            );


        } catch (IOException e) {

            throw new IllegalStateException(
                    "공지사항 첨부파일 저장 중 오류가 발생했습니다.",
                    e
            );
        }
    }


    /**
     * 실제 첨부파일 삭제
     */
    private void deletePhysicalFile(
            Notice notice
    ) {

        if (
                notice.getFileSaved() == null ||
                notice.getFileSaved().isBlank()
        ) {

            return;
        }


        try {

            Path filePath =
                    Paths.get(
                            NOTICE_UPLOAD_DIR,
                            notice.getFileSaved()
                    )
                    .toAbsolutePath()
                    .normalize();


            Files.deleteIfExists(
                    filePath
            );


            notice.removeFile();


        } catch (IOException e) {

            throw new IllegalStateException(
                    "공지사항 첨부파일 삭제 중 오류가 발생했습니다.",
                    e
            );
        }
    }
    
    /**
     * 일반 사용자 공지사항 첨부파일 다운로드
     */
    @Transactional(readOnly = true)
    public NoticeFileDownload loadFile(
            Long noticeId
    ) {

        Notice notice =
                findNotice(
                        noticeId
                );


        /**
         * 비공개 공지사항은
         * 일반 사용자 다운로드 불가
         */
        if (
                !"Y".equals(
                        notice.getVisible()
                )
        ) {

            throw new IllegalStateException(
                    "공개되지 않은 공지사항입니다."
            );
        }


        /**
         * 첨부파일 존재 여부 확인
         */
        if (
                notice.getFileSaved() == null ||
                notice.getFileSaved().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "첨부파일이 존재하지 않습니다."
            );
        }


        try {

            Path filePath =
                    Paths.get(
                            NOTICE_UPLOAD_DIR,
                            notice.getFileSaved()
                    )
                    .toAbsolutePath()
                    .normalize();


            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );


            if (
                    !resource.exists() ||
                    !resource.isReadable()
            ) {

                throw new IllegalArgumentException(
                        "첨부파일을 찾을 수 없습니다."
                );
            }


            return new NoticeFileDownload(
                    resource,
                    notice.getFileOrigin()
            );


        } catch (
                MalformedURLException e
        ) {

            throw new IllegalStateException(
                    "첨부파일을 불러오는 중 오류가 발생했습니다.",
                    e
            );
        }
    }


    /**
     * 첨부파일 다운로드 정보
     */
    public record NoticeFileDownload(

            Resource resource,

            String originalFileName

    ) {
    }


    /**
     * 작성자 이름 정리
     *
     * 비어 있으면 관리자
     */
    private String normalizeWriterName(
            String writerName
    ) {

        if (
                writerName == null ||
                writerName.isBlank()
        ) {

            return "관리자";
        }


        return writerName.trim();
    }


    /**
     * 선택 입력 문자열 정리
     *
     * 빈 문자열은 NULL로 저장
     */
    private String normalizeOptionalText(
            String value
    ) {

        if (
                value == null ||
                value.isBlank()
        ) {

            return null;
        }


        return value.trim();
    }


    /**
     * Y/N 값 정리
     */
    private String normalizeYn(
            String value
    ) {

        return "Y".equalsIgnoreCase(
                value
        )
                ? "Y"
                : "N";
    }
}