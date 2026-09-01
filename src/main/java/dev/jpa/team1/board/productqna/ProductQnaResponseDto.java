package dev.jpa.team1.board.productqna;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaResponseDto {
    private Long id;

    private Long productId;

    private Long memberId;

    private String memberNickname;

    private String title;

    private String content;

    private String secret;

    private String answer;

    private Long answererId;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime answerDate;


    public static ProductQnaResponseDto from(ProductQna qna) {
        return new ProductQnaResponseDto(
                qna.getId(),
                qna.getProduct().getId(),
                qna.getMember().getId(),
                qna.getMember().getNickname(),
                qna.getTitle(),
                qna.getContent(),
                qna.getSecret(),
                qna.getAnswer(),
                qna.getAnswerer() != null ? qna.getAnswerer().getId() : null,
                qna.getStatus(),
                qna.getCreatedAt(),
                qna.getAnswerDate()
        );
    }

}
