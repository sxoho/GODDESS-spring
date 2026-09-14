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
     * 얼굴 중심에서 코가 벗어난 비율
     *
     * 값이 작을수록 정면에 가깝다.
     */
    private Double faceCenterOffset;


    /**
     * 이미지 평균 밝기 값
     */
    private Double brightness;


    /**
     * 사진이 너무 어두운지 여부
     */
    private Boolean isTooDark;


    /**
     * 사진이 너무 밝은지 여부
     */
    private Boolean isTooBright;


    /**
     * 이미지 선명도 값
     *
     * Laplacian 분산값을 사용한다.
     * 값이 낮을수록 흐린 사진이다.
     */
    private Double blurScore;


    /**
     * 사진 흐림 여부
     */
    private Boolean isBlurred;


    /**
     * AI 뷰티 체험용 회원 사진으로
     * 최종 사용 가능한지 여부
     */
    private Boolean valid;


    /**
     * 사용자에게 보여줄 안내 문구
     */
    private String message;
}