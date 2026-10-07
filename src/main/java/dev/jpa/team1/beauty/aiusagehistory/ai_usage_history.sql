-- SQL을 직접 적용하는 경우 테스트

/************************************************/
/* Table Name: AI 사용 이력 */
/************************************************/


-- 기존 테이블 삭제
DROP TABLE AI_USAGE_HISTORY CASCADE CONSTRAINTS;


-- 기존 시퀀스 삭제
DROP SEQUENCE AI_USAGE_HISTORY_SEQ;



-- AI 사용 이력 테이블 생성
CREATE TABLE AI_USAGE_HISTORY (

    AI_USAGE_HISTORY_ID NUMBER PRIMARY KEY,          -- AI 사용 이력 번호
    MEMBER_ID           NUMBER NOT NULL,             -- 회원 번호
    USAGE_TYPE          VARCHAR2(30) NOT NULL,       -- 사용 종류
    USED_AT             TIMESTAMP DEFAULT SYSDATE NOT NULL, -- 사용 일시

    CONSTRAINT FK_AI_USAGE_HISTORY_MEMBER
        FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER(MEMBER_ID),

    CONSTRAINT CK_AI_USAGE_HISTORY_TYPE
        CHECK (USAGE_TYPE IN ('HAIR_STYLE', 'HAIR_COLOR', 'HAIR_BOTH'))
);



-- AI 사용 이력 시퀀스 생성
CREATE SEQUENCE AI_USAGE_HISTORY_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999
    CACHE 2
    NOCYCLE;



-- 코멘트
COMMENT ON TABLE AI_USAGE_HISTORY IS 'AI 사용 이력';

COMMENT ON COLUMN AI_USAGE_HISTORY.AI_USAGE_HISTORY_ID IS 'AI 사용 이력 번호';
COMMENT ON COLUMN AI_USAGE_HISTORY.MEMBER_ID IS '회원 번호';
COMMENT ON COLUMN AI_USAGE_HISTORY.USAGE_TYPE IS 'AI 헤어 사용 종류(HAIR_STYLE, HAIR_COLOR, HAIR_BOTH)';
COMMENT ON COLUMN AI_USAGE_HISTORY.USED_AT IS 'AI 사용 일시';


COMMIT;


SELECT *
FROM AI_USAGE_HISTORY;

/************************************************/
/* 1. 테스트 데이터 등록 */
/************************************************/

-- MEMBER_ID는 실제 MEMBER 테이블에 존재하는 회원 번호 사용
INSERT INTO AI_USAGE_HISTORY (
    AI_USAGE_HISTORY_ID,
    MEMBER_ID,
    USAGE_TYPE,
    USED_AT
)
VALUES (
    AI_USAGE_HISTORY_SEQ.NEXTVAL,
    4,
    'HAIR_STYLE',
    SYSDATE
);

COMMIT;



/************************************************/
/* 2. 전체 AI 사용 이력 조회 */
/************************************************/

SELECT
    AI_USAGE_HISTORY_ID,
    MEMBER_ID,
    USAGE_TYPE,
    USED_AT
FROM AI_USAGE_HISTORY
ORDER BY AI_USAGE_HISTORY_ID DESC;



/************************************************/
/* 3. 특정 회원 AI 사용 이력 조회 */
/************************************************/

SELECT
    AI_USAGE_HISTORY_ID,
    MEMBER_ID,
    USAGE_TYPE,
    USED_AT
FROM AI_USAGE_HISTORY
WHERE MEMBER_ID = 4
ORDER BY USED_AT DESC;



/************************************************/
/* 4. 특정 회원 이번 달 헤어 AI 사용 횟수 조회 */
/************************************************/

SELECT COUNT(*) AS CNT
FROM AI_USAGE_HISTORY
WHERE MEMBER_ID = 4
  AND USED_AT >= TRUNC(SYSDATE, 'MM')
  AND USED_AT < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1);



/************************************************/
/* 5. 특정 회원 이번 달 남은 무료 횟수 확인 */
/************************************************/

SELECT
    3 - COUNT(*) AS REMAINING_COUNT
FROM AI_USAGE_HISTORY
WHERE MEMBER_ID = 4
  AND USED_AT >= TRUNC(SYSDATE, 'MM')
  AND USED_AT < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1);



/************************************************/
/* 6. 테이블 생성 여부 확인 */
/************************************************/

SELECT TABLE_NAME
FROM USER_TABLES
WHERE TABLE_NAME = 'AI_USAGE_HISTORY';



/************************************************/
/* 7. 시퀀스 생성 여부 확인 */
/************************************************/

SELECT SEQUENCE_NAME
FROM USER_SEQUENCES
WHERE SEQUENCE_NAME = 'AI_USAGE_HISTORY_SEQ';



/************************************************/
/* 8. 최종 전체 조회 */
/************************************************/

SELECT *
FROM AI_USAGE_HISTORY
ORDER BY AI_USAGE_HISTORY_ID DESC;

DESC AI_USAGE_HISTORY;


------------------------------------------------------
SELECT USER FROM DUAL;

SELECT OWNER, TABLE_NAME
FROM ALL_TABLES
WHERE TABLE_NAME = 'AI_USAGE_HISTORY';

SELECT OWNER, TABLE_NAME
FROM ALL_TABLES
WHERE TABLE_NAME = 'SUBSCRIPTION';

SELECT SEQUENCE_NAME
FROM USER_SEQUENCES
WHERE SEQUENCE_NAME = 'SUBSCRIPTION_SEQ';
