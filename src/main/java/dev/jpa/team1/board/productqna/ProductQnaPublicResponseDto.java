package dev.jpa.team1.board.productqna;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProductQnaPublicResponseDto {

    private Long id;
    private Long productId;
    private Long memberId;
    private String memberNickname;
    private String title;
    private String content;
    private String secret;
    private String answer;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime answerDate;

    /**
     * 상품 상세 사용자용 DTO
     *
     * 공개글:
     * - 누구나 제목/내용/답변 확인 가능
     *
     * 비밀글:
     * - 작성자 본인만 제목/내용/답변 확인 가능
     * - 다른 사용자는 제목/내용/답변 숨김
     *
     * 관리자 화면은 ProductQnaResponseDto를 사용하므로
     * 비밀글 원문을 그대로 확인할 수 있다.
     */
    public static ProductQnaPublicResponseDto from(
            ProductQna qna,
            Long viewerMemberId
    ) {

        boolean secret =
                "Y".equals(
                        qna.getSecret()
                );

        boolean owner =
                viewerMemberId != null &&
                qna.getMember() != null &&
                qna.getMember()
                        .getId()
                        .equals(
                                viewerMemberId
                        );

        boolean canView =
                !secret || owner;

        String nickname =
                qna.getMember() != null
                        ? qna.getMember()
                                .getNickname()
                        : null;

        return ProductQnaPublicResponseDto
                .builder()
                .id(qna.getId())
                .productId(
                        qna.getProduct()
                                .getId()
                )
                .memberId(
                        qna.getMember()
                                .getId()
                )
                .memberNickname(
                        maskNickname(
                                nickname
                        )
                )
                .title(
                        canView
                                ? qna.getTitle()
                                : "비밀글입니다."
                )
                .content(
                        canView
                                ? qna.getContent()
                                : null
                )
                .secret(
                        qna.getSecret()
                )
                .answer(
                        canView
                                ? qna.getAnswer()
                                : null
                )
                .status(
                        qna.getStatus()
                )
                .createdAt(
                        qna.getCreatedAt()
                )
                .answerDate(
                        canView
                                ? qna.getAnswerDate()
                                : null
                )
                .build();
    }

    private static String maskNickname(
            String nickname
    ) {

        if (
                nickname == null ||
                nickname.isBlank()
        ) {
            return "-";
        }

        if (
                nickname.length() == 1
        ) {
            return nickname;
        }

        return nickname
                .substring(
                        0,
                        1
                )
                + "*".repeat(
                        nickname.length() - 1
                );
    }
}
