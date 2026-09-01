package dev.jpa.team1.board.notice;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;


    /* =====================================================
       일반 사용자 NOTICE
    ===================================================== */


    /**
     * 공개 공지사항 목록
     */
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>>
    findPublicList() {

        return ResponseEntity.ok(
                noticeService.findPublicList()
        );
    }


    /**
     * 공개 공지사항 상세
     *
     * 상세 조회 시 조회수 +1
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto>
    findPublicDetail(
            @PathVariable("noticeId")
            Long noticeId
    ) {

        return ResponseEntity.ok(
                noticeService.findPublicDetail(
                        noticeId
                )
        );
    }


    /**
     * 공개 공지사항 첨부파일 다운로드
     */
    @GetMapping("/{noticeId}/file")
    public ResponseEntity<Resource>
    downloadFile(
            @PathVariable("noticeId")
            Long noticeId
    ) {

        NoticeService.NoticeFileDownload file =
                noticeService.loadFile(
                        noticeId
                );


        String originalFileName =
                file.originalFileName();


        if (
                originalFileName == null ||
                originalFileName.isBlank()
        ) {

            originalFileName =
                    "attachment";
        }


        ContentDisposition contentDisposition =
                ContentDisposition
                        .attachment()
                        .filename(
                                originalFileName,
                                StandardCharsets.UTF_8
                        )
                        .build();


        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        contentDisposition.toString()
                )
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .body(
                        file.resource()
                );
    }


    /* =====================================================
       관리자 NOTICE
    ===================================================== */


    /**
     * 관리자 전체 공지 목록
     *
     * 공개 / 비공개 모두 조회
     */
    @GetMapping("/admin/list")
    public ResponseEntity<List<NoticeResponseDto>>
    findAdminList() {

        return ResponseEntity.ok(
                noticeService.findAdminList()
        );
    }


    /**
     * 관리자 공지 상세
     *
     * 조회수 증가 없음
     */
    @GetMapping("/admin/{noticeId}")
    public ResponseEntity<NoticeResponseDto>
    findAdminDetail(
            @PathVariable("noticeId")
            Long noticeId
    ) {

        return ResponseEntity.ok(
                noticeService.findAdminDetail(
                        noticeId
                )
        );
    }


    /**
     * 공지사항 등록
     *
     * JSON 데이터 + 첨부파일
     */
    @PostMapping(
            value = "/admin",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<NoticeResponseDto>
    save(
            @Valid
            @RequestPart("request")
            NoticeRequestDto requestDto,

            @RequestPart(
                    value = "file",
                    required = false
            )
            MultipartFile file
    ) {

        return ResponseEntity.ok(
                noticeService.save(
                        requestDto,
                        file
                )
        );
    }


    /**
     * 공지사항 수정
     *
     * JSON 데이터 + 첨부파일
     */
    @PutMapping(
            value = "/admin/{noticeId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<NoticeResponseDto>
    update(
            @PathVariable("noticeId")
            Long noticeId,

            @Valid
            @RequestPart("request")
            NoticeRequestDto requestDto,

            @RequestPart(
                    value = "file",
                    required = false
            )
            MultipartFile file
    ) {

        return ResponseEntity.ok(
                noticeService.update(
                        noticeId,
                        requestDto,
                        file
                )
        );
    }


    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/admin/{noticeId}")
    public ResponseEntity<Void>
    delete(
            @PathVariable("noticeId")
            Long noticeId,

            @RequestParam("adminId")
            Long adminId
    ) {

        noticeService.delete(
                noticeId,
                adminId
        );


        return ResponseEntity
                .noContent()
                .build();
    }
}