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


            if (
                    body == null
                    ||
                    body.getMessage() == null
                    ||
                    body.getMessage().isBlank()
            ) {


                return new AiAnswer(

                        "답변을 생성하지 못했습니다. 잠시 후 다시 질문해주세요.",

                        null,

                        null
                );
            }


            return new AiAnswer(

                    body
                            .getMessage()
                            .trim(),

                    body.getAction(),

                    body.getProductRecommendation()
            );


        } catch (
                RestClientException e
        ) {


            System.err.println(

                    "FastAPI 챗봇 호출 오류: "
                            + e.getMessage()
            );


            return new AiAnswer(

                    "현재 AI 챗봇 서버와 연결할 수 없습니다. 잠시 후 다시 이용해주세요.",

                    null,

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

        private String skinType;

        private String personalColor;
    }


    /*
     * =========================================
     * FastAPI 상품 추천 조건
     *
     * 예:
     *
     * {
     *   "personalColor": "SUMMER_COOL",
     *   "productType": "LIP",
     *   "limit": 3
     * }
     * =========================================
     */
    @Getter
    @NoArgsConstructor
    public static class ProductRecommendation {

        private String personalColor;

        private String productType;

        private Integer limit;
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

        private ProductRecommendation productRecommendation;
    }


    /*
     * =========================================
     * ChatMessageService로 전달
     * =========================================
     */
    @Getter
    @AllArgsConstructor
    public static class AiAnswer {

        private String message;

        private ChatbotActionDto action;

        private ProductRecommendation productRecommendation;
    }
}