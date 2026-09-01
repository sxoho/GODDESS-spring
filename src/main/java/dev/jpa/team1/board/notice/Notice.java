package dev.jpa.team1.board.notice;

import dev.jpa.team1.member.member.Member;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notice_seq"
    )
    @SequenceGenerator(
            name = "notice_seq",
            sequenceName = "notice_seq",
            allocationSize = 1
    )
    @Column(name = "notice_id")
    private Long id;


    /**
     * 공지 작성 관리자
     *
     * 관리자도 MEMBER 테이블에서 관리하므로
     * MEMBER_ID를 외래키로 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member admin;


    /**
     * 화면에 표시할 작성자 이름
     *
     * 기본값은 관리자
     * 필요 시 박예은 등으로 직접 수정 가능
     */
    @Column(
            name = "writer_name",
            nullable = false,
            length = 100
    )
    @Builder.Default
    private String writerName = "관리자";


    @Column(
            nullable = false,
            length = 255
    )
    private String title;


    @Lob
    @Column(nullable = false)
    private String content;


    /**
     * 관련 링크
     */
    @Column(
            name = "link_url",
            length = 1000
    )
    private String linkUrl;


    /**
     * 첨부파일 원본 파일명
     */
    @Column(
            name = "file_origin",
            length = 255
    )
    private String fileOrigin;


    /**
     * 서버에 저장되는 파일명
     */
    @Column(
            name = "file_saved",
            length = 255
    )
    private String fileSaved;


    /**
     * 첨부파일 저장 경로
     */
    @Column(
            name = "file_path",
            length = 500
    )
    private String filePath;


    /**
     * 첨부파일 크기
     */
    @Column(name = "file_size")
    private Long fileSize;


    @Column(nullable = false)
    @Builder.Default
    private Integer viewCount = 0;


    /**
     * Y = 상단 고정
     * N = 일반 공지
     */
    @Column(
            nullable = false,
            length = 1
    )
    private String fixed;


    /**
     * Y = 공개
     * N = 비공개
     */
    @Column(
            nullable = false,
            length = 1
    )
    private String visible;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    private LocalDateTime updatedAt;


    /**
     * 공지사항 수정
     */
    public void update(
            String writerName,
            String title,
            String content,
            String linkUrl,
            String fixed,
            String visible
    ) {

        this.writerName = writerName;
        this.title = title;
        this.content = content;
        this.linkUrl = linkUrl;
        this.fixed = fixed;
        this.visible = visible;
    }


    /**
     * 첨부파일 정보 등록 / 변경
     */
    public void updateFile(
            String fileOrigin,
            String fileSaved,
            String filePath,
            Long fileSize
    ) {

        this.fileOrigin = fileOrigin;
        this.fileSaved = fileSaved;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }


    /**
     * 첨부파일 정보 삭제
     */
    public void removeFile() {

        this.fileOrigin = null;
        this.fileSaved = null;
        this.filePath = null;
        this.fileSize = null;
    }


    /**
     * 조회수 증가
     */
    public void increaseViewCount() {

        if (this.viewCount == null) {
            this.viewCount = 0;
        }

        this.viewCount++;
    }
}