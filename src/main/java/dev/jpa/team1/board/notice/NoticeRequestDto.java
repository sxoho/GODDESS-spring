package dev.jpa.team1.board.notice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDto {

    @NotNull
    private Long adminId;


    /**
     * 화면에 표시할 작성자 이름
     *
     * 기본값은 "관리자"
     * 필요 시 직접 수정 가능
     */
    @NotBlank
    @Size(max = 100)
    private String writerName = "관리자";


    @NotBlank
    @Size(max = 255)
    private String title;


    @NotBlank
    private String content;


    /**
     * 관련 링크
     *
     * 선택 입력
     */
    @Size(max = 1000)
    private String linkUrl;


    @NotBlank
    @Size(max = 1)
    private String fixed;


    @NotBlank
    @Size(max = 1)
    private String visible;
}