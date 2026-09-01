package dev.jpa.team1.board.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {

    private Long id;

    private Long adminId;

    private String adminName;

    /**
     * 화면에 표시할 작성자 이름
     */
    private String writerName;

    private String title;

    private String content;

    /**
     * 관련 링크
     */
    private String linkUrl;

    /**
     * 첨부파일 원본 파일명
     */
    private String fileOrigin;

    /**
     * 서버 저장 파일명
     */
    private String fileSaved;

    /**
     * 첨부파일 저장 경로
     */
    private String filePath;

    /**
     * 첨부파일 크기
     */
    private Long fileSize;

    private Integer viewCount;

    private String fixed;

    private String visible;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static NoticeResponseDto from(
            Notice notice
    ) {

        return new NoticeResponseDto(

                notice.getId(),

                notice.getAdmin().getId(),

                notice.getAdmin().getName(),

                notice.getWriterName(),

                notice.getTitle(),

                notice.getContent(),

                notice.getLinkUrl(),

                notice.getFileOrigin(),

                notice.getFileSaved(),

                notice.getFilePath(),

                notice.getFileSize(),

                notice.getViewCount(),

                notice.getFixed(),

                notice.getVisible(),

                notice.getCreatedAt(),

                notice.getUpdatedAt()
        );
    }
}