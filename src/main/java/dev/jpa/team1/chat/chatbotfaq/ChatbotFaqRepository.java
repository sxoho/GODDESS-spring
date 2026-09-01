package dev.jpa.team1.chat.chatbotfaq;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 챗봇 FAQ Repository
 *
 * CHATBOT_FAQ 테이블의 등록, 조회, 수정, 삭제를 담당한다.
 *
 * 자주 묻는 질문과 답변을 카테고리별로 관리하며,
 * 챗봇이 사용자 질문에 응답할 때 활용할 수 있다.
 *
 * ChatbotFaq의 categoryId는 Category Entity 연관관계가 아니라
 * 단순 Long 타입 필드로 저장되어 있다.
 *
 * Repository 자체에는 직접 실행 URL이 없다.
 *
 * 추후 Controller 연결 예시:
 * POST   http://localhost:포트번호/api/chatbot-faqs
 * GET    http://localhost:포트번호/api/chatbot-faqs
 * GET    http://localhost:포트번호/api/chatbot-faqs/{faqId}
 * PATCH  http://localhost:포트번호/api/chatbot-faqs/{faqId}
 * DELETE http://localhost:포트번호/api/chatbot-faqs/{faqId}
 */
public interface ChatbotFaqRepository
        extends JpaRepository<ChatbotFaq, Long> {

    /**
     * 공개 중인 전체 FAQ 조회
     *
     * 최근 등록된 FAQ부터 조회한다.
     *
     * visible 예시:
     * Y = 공개
     * N = 비공개
     */
    List<ChatbotFaq> findByVisibleOrderByCreatedAtDesc(
            String visible
    );

    /**
     * 특정 카테고리의 전체 FAQ 조회
     */
    List<ChatbotFaq>
            findByCategoryIdOrderByCreatedAtDesc(Long categoryId);

    /**
     * 특정 카테고리에서 공개 중인 FAQ 조회
     */
    List<ChatbotFaq>
            findByCategoryIdAndVisibleOrderByCreatedAtDesc(
                    Long categoryId,
                    String visible
            );

    /**
     * 질문 내용에 검색어가 포함된 FAQ 조회
     *
     * 대소문자를 구분하지 않는다.
     */
    List<ChatbotFaq>
            findByQuestionContainingIgnoreCaseOrderByCreatedAtDesc(
                    String question
            );

    /**
     * 공개 FAQ 중 질문 내용에 검색어가 포함된 목록 조회
     */
    List<ChatbotFaq>
            findByVisibleAndQuestionContainingIgnoreCaseOrderByCreatedAtDesc(
                    String visible,
                    String question
            );

    /**
     * 특정 카테고리의 공개 FAQ 중
     * 질문 내용에 검색어가 포함된 목록 조회
     */
    List<ChatbotFaq>
            findByCategoryIdAndVisibleAndQuestionContainingIgnoreCaseOrderByCreatedAtDesc(
                    Long categoryId,
                    String visible,
                    String question
            );

    /**
     * 질문 내용이 정확히 일치하는 FAQ 한 건 조회
     */
    Optional<ChatbotFaq> findByQuestion(String question);

    /**
     * 같은 질문이 이미 존재하는지 확인
     */
    boolean existsByQuestion(String question);

    /**
     * 조회수가 높은 순서로 전체 FAQ 조회
     */
    List<ChatbotFaq> findAllByOrderByViewCountDesc();

    /**
     * 공개 FAQ를 조회수가 높은 순서로 조회
     */
    List<ChatbotFaq>
            findByVisibleOrderByViewCountDesc(String visible);

    /**
     * 특정 카테고리의 FAQ를 조회수가 높은 순서로 조회
     */
    List<ChatbotFaq>
            findByCategoryIdOrderByViewCountDesc(Long categoryId);

    /**
     * 지정한 조회수 이상의 FAQ 조회
     */
    List<ChatbotFaq>
            findByViewCountGreaterThanEqualOrderByViewCountDesc(
                    Integer viewCount
            );

    /**
     * 특정 카테고리의 FAQ 개수 조회
     */
    long countByCategoryId(Long categoryId);

    /**
     * 공개 중인 FAQ 개수 조회
     */
    long countByVisible(String visible);

    /**
     * 특정 카테고리의 공개 FAQ 개수 조회
     */
    long countByCategoryIdAndVisible(
            Long categoryId,
            String visible
    );

    /// 사용 여부에 따라서 아이디 내림차순 조회
    List<ChatbotFaq> findByVisibleOrderByIdDesc(String visible);
}