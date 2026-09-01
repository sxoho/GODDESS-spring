package dev.jpa.team1.member.memberphoto;

import dev.jpa.team1.member.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "member_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberPhoto {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_photo_seq"
    )
    @SequenceGenerator(
            name = "member_photo_seq",
            sequenceName = "member_photo_seq",
            allocationSize = 1
    )
    @Column(name = "member_photo_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member member;


    // 현재는 얼굴 사진만 사용
    // 추후 필요 시 사진 종류 확장 가능
    @Column(
            name = "photo_type",
            nullable = false,
            length = 50
    )
    private String photoType;


    // 사용자가 업로드한 원본 파일명
    @Column(
            name = "file_origin",
            nullable = false,
            length = 255
    )
    private String fileOrigin;


    // 서버에 실제 저장되는 파일명
    @Column(
            name = "file_saved",
            nullable = false,
            length = 255
    )
    private String fileSaved;


    // 브라우저에서 이미지에 접근할 경로
    @Column(
            name = "file_path",
            nullable = false,
            length = 500
    )
    private String filePath;


    // 파일 크기
    @Column(name = "file_size")
    private Long fileSize;


    // 대표 사진 여부
    @Column(
            name = "is_main",
            nullable = false,
            length = 1
    )
    private String isMain;


    // 사용 여부
    @Column(
            name = "visible",
            nullable = false,
            length = 1
    )
    private String visible;


    @CreationTimestamp
    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    /**
     * 기존 대표 사진 해제
     */
    public void removeMain() {
        this.isMain = "N";
    }


    /**
     * 대표 사진으로 변경
     */
    public void setMain() {
        this.isMain = "Y";
        this.visible = "Y";
    }


    /**
     * 사진 사용 중지
     */
    public void hide() {
        this.visible = "N";
        this.isMain = "N";
    }


    /**
     * 사진 다시 사용
     */
    public void show() {
        this.visible = "Y";
    }
}