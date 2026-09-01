-- ChatbotFaq 테이블 및 시퀀스 생성

CREATE SEQUENCE chatbot_faq_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE chatbot_faq (
    faq_id NUMBER PRIMARY KEY, -- FAQ 번호 (기본키)
    category_id NUMBER NOT NULL, -- 카테고리 번호
    question VARCHAR2(500) NOT NULL, -- 질문/키워드
    answer CLOB NOT NULL, -- 답변 내용
    view_count NUMBER DEFAULT 0, -- 조회/활용 횟수
    visible VARCHAR2(1) NOT NULL, -- 사용 여부(Y/N)
    created_at TIMESTAMP DEFAULT SYSDATE, -- 등록일
    updated_at TIMESTAMP -- 수정일
);

-- 코멘트
COMMENT ON TABLE chatbot_faq IS '챗봇 FAQ 지식베이스';
COMMENT ON COLUMN chatbot_faq.faq_id IS 'FAQ 번호';
COMMENT ON COLUMN chatbot_faq.category_id IS '카테고리 번호';
COMMENT ON COLUMN chatbot_faq.question IS '질문/키워드';
COMMENT ON COLUMN chatbot_faq.answer IS '답변 내용';
COMMENT ON COLUMN chatbot_faq.view_count IS '조회/활용 횟수';
COMMENT ON COLUMN chatbot_faq.visible IS '사용 여부(Y/N)';
COMMENT ON COLUMN chatbot_faq.created_at IS '등록일';
COMMENT ON COLUMN chatbot_faq.updated_at IS '수정일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO chatbot_faq (faq_id, category_id, question, answer, view_count, visible, created_at, updated_at) VALUES (chatbot_faq_seq.NEXTVAL, 1, '배송은 얼마나 걸리나요?', '영업일 기준 1~3일 소요됩니다.', 1, 'Y', SYSDATE, SYSDATE);
INSERT INTO chatbot_faq (faq_id, category_id, question, answer, view_count, visible, created_at, updated_at) VALUES (chatbot_faq_seq.NEXTVAL, 2, '교환/반품은 어떻게 하나요?', '마이페이지 > 주문내역에서 교환/반품 신청이 가능합니다.', 2, 'Y', SYSDATE, SYSDATE);
INSERT INTO chatbot_faq (faq_id, category_id, question, answer, view_count, visible, created_at, updated_at) VALUES (chatbot_faq_seq.NEXTVAL, 3, '회원 탈퇴는 어디서 하나요?', '마이페이지 > 내 정보 수정 하단에서 탈퇴하실 수 있습니다.', 3, 'Y', SYSDATE, SYSDATE);
COMMIT;
