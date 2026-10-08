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
     * =========================================
     * FastAPI 챗봇 서버
     * =========================================
     *
     * 팀5 포트 범위:
     * 11500 ~ 11599
     *
     * AI BEAUTY:
     * 11502
     *
     * LangChain RAG 챗봇:
     * 11503
     * =========================================
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
     *
     * Spring
     * -> FastAPI
     *
     * 전달 값:
     * roomId
     * message
     * skinType
     * personalColor
     * =========================================
     */
    public AiAnswer ask(

            Long roomId,

            String message,

            String skinType,

            String personalColor

    ) {


        FastApiAskRequest request =
                new FastApiAskRequest(

                        roomId,

                        message,

                        skinType,

                        personalColor
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


            /*
             * FastAPI 응답이 없거나
             * 답변 문자열이 비어있는 경우
             */
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


            /*
             * 정상 응답
             */
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
             * =====================================
             * FastAPI 연결 오류
             * =====================================
             *
             * 사용자 질문은 이미 DB에 저장된 상태이고
             * FastAPI 호출에 실패한 경우
             * BOT 안내 메시지를 반환한다.
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
     *
     * 실제 JSON:
     *
     * {
     *   "roomId": 1,
     *   "message": "내 피부 타입에 맞는 관리법 알려줘",
     *   "skinType": "DRY",
     *   "personalColor": "SPRING_WARM"
     * }
     * =========================================
     */
    @Getter
    @AllArgsConstructor
    private static class FastApiAskRequest {

        private Long roomId;

        private String message;

        private String skinType;

        private String personalColor;
    }


    /*
     * =========================================
     * FastAPI -> Spring 응답
     *
     * {
     *   "message": "...",
     *   "action": {
     *       "type": "NAVIGATE",
     *       "label": "...",
     *       "path": "..."
     *   }
     * }
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