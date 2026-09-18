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
     * LangGraph에서 어떤 경로로 실행했는지
     *
     * base
     * lip
     * cheek
     * eye
     * combined_beauty
     * ...
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
}