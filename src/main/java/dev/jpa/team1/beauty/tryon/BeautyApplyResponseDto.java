package dev.jpa.team1.beauty.tryon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BeautyApplyResponseDto {

    /**
     * 성공 여부
     */
    private boolean success;


    /**
     * LangGraph 실행 경로
     *
     * base
     * lip
     * cheek
     * eye
     * hair_style
     * hair_color
     * combined_beauty
     */
    private String route;


    /**
     * 처리 상태 메시지
     */
    private String status;


    /**
     * OpenAI 결과 이미지 Base64
     */
    private String resultImageBase64;


    /**
     * 결과 이미지 MIME 타입
     */
    private String resultContentType;


    /**
     * 결과 파일명
     */
    private String resultFileName;


    /**
     * DB 저장된 AI BEAUTY 실행 ID
     *
     * BEAUTY_TRYON.beautytryon_id
     */
    private Long beautyTryonId;


    /**
     * DB 저장된 AI 결과 ID
     *
     * BEAUTY_TRYON_RESULT.beautyresult_id
     *
     * 이후 결과 저장 버튼에서
     * BEAUTY_SAVE 연결에 사용
     */
    private Long beautyResultId;
}