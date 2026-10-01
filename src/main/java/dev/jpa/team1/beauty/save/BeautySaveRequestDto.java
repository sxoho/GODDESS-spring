package dev.jpa.team1.beauty.save;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BeautySaveRequestDto {

    /**
     * 저장하는 회원 ID
     */
    private Long memberId;


    /**
     * 저장할 AI BEAUTY 결과 ID
     *
     * BEAUTY_TRYON_RESULT.beautyresult_id
     */
    private Long beautyResultId;


    /**
     * 저장 이름
     *
     * 값이 없으면 Service에서 기본 이름 생성
     */
    private String saveName;


    /**
     * 메모
     */
    private String memo;
}