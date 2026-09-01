-- StyleRecommendation 테이블 및 시퀀스 생성

CREATE SEQUENCE style_recommendation_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE style_recommendation (
    recommendation_id NUMBER PRIMARY KEY, -- 스타일 추천 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    occasion VARCHAR2(100) NOT NULL, -- 추천 상황(면접, 데이트 등)
    mood VARCHAR2(100) NOT NULL, -- 원하는 분위기
    ai_description CLOB, -- AI 추천 설명
    created_at TIMESTAMP DEFAULT SYSDATE, -- 등록일
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

-- 코멘트
COMMENT ON TABLE style_recommendation IS '스타일 추천';
COMMENT ON COLUMN style_recommendation.recommendation_id IS '스타일 추천 번호';
COMMENT ON COLUMN style_recommendation.member_id IS '회원 번호';
COMMENT ON COLUMN style_recommendation.occasion IS '추천 상황(면접, 데이트 등)';
COMMENT ON COLUMN style_recommendation.mood IS '원하는 분위기';
COMMENT ON COLUMN style_recommendation.ai_description IS 'AI 추천 설명';
COMMENT ON COLUMN style_recommendation.created_at IS '등록일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO style_recommendation (recommendation_id, member_id, occasion, mood, ai_description, created_at) VALUES (style_recommendation_seq.NEXTVAL, 1, '테스트_occasion_1', '테스트_mood_1', '테스트_ai_description_1', SYSDATE);
INSERT INTO style_recommendation (recommendation_id, member_id, occasion, mood, ai_description, created_at) VALUES (style_recommendation_seq.NEXTVAL, 2, '테스트_occasion_2', '테스트_mood_2', '테스트_ai_description_2', SYSDATE);
INSERT INTO style_recommendation (recommendation_id, member_id, occasion, mood, ai_description, created_at) VALUES (style_recommendation_seq.NEXTVAL, 3, '테스트_occasion_3', '테스트_mood_3', '테스트_ai_description_3', SYSDATE);
COMMIT;
