package dev.jpa.team1.member.memberphoto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPhotoResponseDto {

    private Long id;

    private Long memberId;

    private String photoType;

    private String fileOrigin;

    private String fileSaved;

    private String filePath;

    private Long fileSize;

    private String isMain;

    private String visible;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static MemberPhotoResponseDto from(
            MemberPhoto photo
    ) {

        return new MemberPhotoResponseDto(
                photo.getId(),
                photo.getMember().getId(),
                photo.getPhotoType(),
                photo.getFileOrigin(),
                photo.getFileSaved(),
                photo.getFilePath(),
                photo.getFileSize(),
                photo.getIsMain(),
                photo.getVisible(),
                photo.getCreatedAt(),
                photo.getUpdatedAt()
        );
    }
}