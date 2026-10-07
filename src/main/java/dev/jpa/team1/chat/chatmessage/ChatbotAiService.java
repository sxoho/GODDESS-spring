package dev.jpa.team1.chat.chatmessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotAiService {

    private final RestTemplate restTemplate;


    /*
     * 다음 단계에서 만들 FastAPI 챗봇 서버
     *
     * 팀5 포트 범위:
     * 11500 ~ 11599
     *
     * 기존 AI BEAUTY:
     * 11502
     *
     * 챗봇:
     * 11503 예정
     */
    @Value(
            "${chatbot.fastapi.url:http://139.150.91.194:11503}"
    )
    private String fastApiBaseUrl;


    public ChatbotAiService() {

        this.restTemplate =
                new RestTemplate();
    }


    /*
     * =========================================
     * FastAPI 챗봇 질문
     * =========================================
     */
    public AiAnswer ask(
            Long roomId,
            String message
    ) {

        FastApiAskRequest request =
                new FastApiAskRequest(
                        roomId,
                        message
                );


        try {

            ResponseEntity<FastApiAskResponse>
                    response =
                    restTemplate.postForEntity(
                            fastApiBaseUrl
                                    + "/chatbot/ask",
                            request,
                            FastApiAskResponse.class
                    );


            FastApiAskResponse body =
                    response.getBody();


            if (
                    body == null
                    ||
                    body.getMessage() == null
                    ||
                    body.getMessage().isBlank()
            ) {

                return new AiAnswer(
                        "답변을 생성하지 못했습니다. 잠시 후 다시 질문해주세요.",
                        null
                );
            }


            return new AiAnswer(
                    body
                            .getMessage()
                            .trim(),

                    body.getAction()
            );


        } catch (
                RestClientException e
        ) {

            /*
             * 아직 FastAPI 챗봇 서버가 실행되지 않았거나
             * 연결에 실패한 경우.
             *
             * 사용자 질문 자체는 DB에 저장하고,
             * BOT에는 연결 실패 안내를 저장한다.
             */
            System.err.println(
                    "FastAPI 챗봇 호출 오류: "
                            + e.getMessage()
            );


            return new AiAnswer(
                    "현재 AI 챗봇 서버와 연결할 수 없습니다. 잠시 후 다시 이용해주세요.",
                    null
            );
        }
    }


    /*
     * =========================================
     * Spring -> FastAPI 요청
     * =========================================
     */
    @Getter
    @AllArgsConstructor
    private static class FastApiAskRequest {

        private Long roomId;

        private String message;
    }


    /*
     * =========================================
     * FastAPI -> Spring 응답
     * =========================================
     */
    @Getter
    @NoArgsConstructor
    private static class FastApiAskResponse {

        private String message;

        private ChatbotActionDto action;
    }


    /*
     * =========================================
     * ChatMessageService로 전달할 결과
     * =========================================
     */
    @Getter
    @AllArgsConstructor
    public static class AiAnswer {

        private String message;

        private ChatbotActionDto action;
    }
}