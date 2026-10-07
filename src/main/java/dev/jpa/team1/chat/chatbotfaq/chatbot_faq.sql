/******************************************************************
Table Name: 챗봇 FAQ
******************************************************************/

DROP TABLE CHATBOT_FAQ CASCADE CONSTRAINTS;
DROP SEQUENCE CHATBOT_FAQ_SEQ;


/******************************************************************
1. Sequence
******************************************************************/

CREATE SEQUENCE CHATBOT_FAQ_SEQ
START WITH 1
INCREMENT BY 1
MAXVALUE 9999999
CACHE 2
NOCYCLE;


/******************************************************************
2. CHATBOT_FAQ 테이블 생성
******************************************************************/

CREATE TABLE CHATBOT_FAQ (
    FAQ_ID         NUMBER         NOT NULL,
    CATEGORY       VARCHAR2(50)   NOT NULL,
    QUESTION       VARCHAR2(500)  NOT NULL,
    ANSWER         CLOB           NOT NULL,
    KEYWORDS       VARCHAR2(500),
    DISPLAY_ORDER  NUMBER         DEFAULT 0 NOT NULL,
    VIEW_COUNT     NUMBER         DEFAULT 0 NOT NULL,
    VISIBLE        VARCHAR2(1)    DEFAULT 'Y' NOT NULL,
    CREATED_AT     TIMESTAMP      DEFAULT SYSDATE,
    UPDATED_AT     TIMESTAMP,

    PRIMARY KEY (FAQ_ID),

    CONSTRAINT CK_CHATBOT_FAQ_VISIBLE
        CHECK (VISIBLE IN ('Y', 'N'))
);


/******************************************************************
3. Comments
******************************************************************/

COMMENT ON TABLE CHATBOT_FAQ IS 'GODDESS 챗봇 FAQ';
COMMENT ON COLUMN CHATBOT_FAQ.FAQ_ID IS 'FAQ 번호';
COMMENT ON COLUMN CHATBOT_FAQ.CATEGORY IS 'FAQ 카테고리';
COMMENT ON COLUMN CHATBOT_FAQ.QUESTION IS '추천 질문';
COMMENT ON COLUMN CHATBOT_FAQ.ANSWER IS 'FAQ 답변';
COMMENT ON COLUMN CHATBOT_FAQ.KEYWORDS IS '검색용 키워드';
COMMENT ON COLUMN CHATBOT_FAQ.DISPLAY_ORDER IS '화면 표시 순서';
COMMENT ON COLUMN CHATBOT_FAQ.VIEW_COUNT IS '조회 횟수';
COMMENT ON COLUMN CHATBOT_FAQ.VISIBLE IS '노출 여부(Y/N)';
COMMENT ON COLUMN CHATBOT_FAQ.CREATED_AT IS '등록일';
COMMENT ON COLUMN CHATBOT_FAQ.UPDATED_AT IS '수정일';

COMMIT;


/******************************************************************
4. CREATE - GODDESS 챗봇 FAQ 기본 데이터
******************************************************************/

INSERT INTO CHATBOT_FAQ (
    FAQ_ID, CATEGORY, QUESTION, ANSWER, KEYWORDS,
    DISPLAY_ORDER, VIEW_COUNT, VISIBLE, CREATED_AT
) VALUES (
    CHATBOT_FAQ_SEQ.NEXTVAL,
    'AI_BEAUTY',
    'AI BEAUTY는 어떻게 이용하나요?',
    'AI BEAUTY 메뉴에서 회원 사진을 선택한 뒤 메이크업 또는 헤어 스타일을 선택하고 AI BEAUTY 적용하기 버튼을 누르면 가상 체험 결과를 확인할 수 있습니다.',
    'AI BEAUTY,가상체험,메이크업,헤어',
    1, 0, 'Y', SYSDATE
);

INSERT INTO CHATBOT_FAQ (
    FAQ_ID, CATEGORY, QUESTION, ANSWER, KEYWORDS,
    DISPLAY_ORDER, VIEW_COUNT, VISIBLE, CREATED_AT
) VALUES (
    CHATBOT_FAQ_SEQ.NEXTVAL,
    'HAIR',
    '헤어 AI는 무료로 몇 번 사용할 수 있나요?',
    '일반 회원은 헤어스타일 또는 헤어컬러가 포함된 AI 결과 생성을 월 3회 무료로 이용할 수 있습니다. 헤어스타일과 헤어컬러를 동시에 적용해도 1회로 계산됩니다.',
    '헤어,헤어스타일,헤어컬러,무료,3회',
    2, 0, 'Y', SYSDATE
);

INSERT INTO CHATBOT_FAQ (
    FAQ_ID, CATEGORY, QUESTION, ANSWER, KEYWORDS,
    DISPLAY_ORDER, VIEW_COUNT, VISIBLE, CREATED_AT
) VALUES (
    CHATBOT_FAQ_SEQ.NEXTVAL,
    'PREMIUM',
    'Premium 이용권 가격은 얼마인가요?',
    'Premium은 월간 5,000원, 연간 40,000원입니다. Premium 이용 중에는 헤어 AI를 횟수 제한 없이 이용할 수 있습니다.',
    'Premium,프리미엄,구독,월간,연간,가격',
    3, 0, 'Y', SYSDATE
);

INSERT INTO CHATBOT_FAQ (
    FAQ_ID, CATEGORY, QUESTION, ANSWER, KEYWORDS,
    DISPLAY_ORDER, VIEW_COUNT, VISIBLE, CREATED_AT
) VALUES (
    CHATBOT_FAQ_SEQ.NEXTVAL,
    'DIAGNOSIS',
    '피부 타입 진단은 어디서 하나요?',
    '마이페이지의 피부 타입 진단 기능에서 피부 관련 질문에 답변하면 진단 결과를 확인하고 저장할 수 있습니다.',
    '피부,피부타입,진단,마이페이지',
    4, 0, 'Y', SYSDATE
);

INSERT INTO CHATBOT_FAQ (
    FAQ_ID, CATEGORY, QUESTION, ANSWER, KEYWORDS,
    DISPLAY_ORDER, VIEW_COUNT, VISIBLE, CREATED_AT
) VALUES (
    CHATBOT_FAQ_SEQ.NEXTVAL,
    'DIAGNOSIS',
    '퍼스널컬러 결과는 어디서 확인하나요?',
    '퍼스널컬러 진단을 완료하면 결과가 저장되며 마이페이지에서 현재 퍼스널컬러 정보를 확인할 수 있습니다.',
    '퍼스널컬러,진단,결과,마이페이지',
    5, 0, 'Y', SYSDATE
);

INSERT INTO CHATBOT_FAQ (
    FAQ_ID, CATEGORY, QUESTION, ANSWER, KEYWORDS,
    DISPLAY_ORDER, VIEW_COUNT, VISIBLE, CREATED_AT
) VALUES (
    CHATBOT_FAQ_SEQ.NEXTVAL,
    'HISTORY',
    '저장한 AI BEAUTY 결과는 어디서 볼 수 있나요?',
    'AI BEAUTY의 MY HISTORY에서 저장한 메이크업과 헤어 가상 체험 결과를 다시 확인할 수 있습니다.',
    'MY HISTORY,이용내역,저장,AI BEAUTY',
    6, 0, 'Y', SYSDATE
);

INSERT INTO CHATBOT_FAQ (
    FAQ_ID, CATEGORY, QUESTION, ANSWER, KEYWORDS,
    DISPLAY_ORDER, VIEW_COUNT, VISIBLE, CREATED_AT
) VALUES (
    CHATBOT_FAQ_SEQ.NEXTVAL,
    'MEMBER',
    '회원 사진은 어디서 변경하나요?',
    '마이페이지의 회원정보 관리에서 등록된 회원 사진을 확인하고 변경할 수 있습니다.',
    '회원사진,사진변경,마이페이지,회원정보',
    7, 0, 'Y', SYSDATE
);

COMMIT;


/******************************************************************
5. READ - 확인
******************************************************************/

SELECT *
FROM CHATBOT_FAQ
ORDER BY DISPLAY_ORDER ASC, FAQ_ID ASC;
