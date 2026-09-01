package dev.jpa.team1.board.customerqna;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerQnaResponseDto {
    private Long id;

    private Long memberId;

    private String memberNickname;

    private String category;

    private Long orderId;

    private String title;

    private String content;

    private String answer;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime answerDate;


    public static CustomerQnaResponseDto from(CustomerQna qna) {
        return new CustomerQnaResponseDto(
                qna.getId(),
                qna.getMember().getId(),
                qna.getMember().getNickname(),
                qna.getCategory(),
                qna.getOrders() != null ? qna.getOrders().getId() : null,
                qna.getTitle(),
                qna.getContent(),
                qna.getAnswer(),
                qna.getStatus(),
                qna.getCreatedAt(),
                qna.getAnswerDate()
        );
    }

}
