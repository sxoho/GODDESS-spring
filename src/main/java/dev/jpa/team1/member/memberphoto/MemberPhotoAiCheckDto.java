package dev.jpa.team1.member.memberphoto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPhotoAiCheckDto {

    /**
     * 얼굴 검출 여부
     */
    private Boolean faceDetected;


    /**
     * 정면 얼굴 여부
     */
    private Boolean isFront;


    /**
     * 사용자에게 보여줄 안내 문구
     */
    private String message;
}