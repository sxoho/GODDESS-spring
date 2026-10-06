-- SQL을 직접 적용하는 경우 테스트

/************************************************/
/* Table Name: 구독 */
/************************************************/


-- 기존 테이블 삭제
DROP TABLE SUBSCRIPTION CASCADE CONSTRAINTS;


-- 기존 시퀀스 삭제
DROP SEQUENCE SUBSCRIPTION_SEQ;



-- 구독 테이블 생성
CREATE TABLE SUBSCRIPTION (

    SUBSCRIPTION_ID NUMBER PRIMARY KEY,             -- 구독 번호
    MEMBER_ID       NUMBER NOT NULL,                -- 회원 번호
    PLAN_TYPE       VARCHAR2(20) NOT NULL,          -- 구독 플랜
    STATUS          VARCHAR2(20) DEFAULT 'ACTIVE' NOT NULL, -- 구독 상태
    PRICE           NUMBER NOT NULL,                -- 결제 금액
    START_DATE      TIMESTAMP DEFAULT SYSDATE NOT NULL, -- 구독 시작일
    END_DATE        TIMESTAMP NOT NULL,             -- 구독 종료일
    CREATED_AT      TIMESTAMP DEFAULT SYSDATE NOT NULL,
    UPDATED_AT      TIMESTAMP,

    CONSTRAINT FK_SUBSCRIPTION_MEMBER
        FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER(MEMBER_ID),

    CONSTRAINT CK_SUBSCRIPTION_PLAN
        CHECK (PLAN_TYPE IN ('MONTHLY', 'YEARLY')),

    CONSTRAINT CK_SUBSCRIPTION_STATUS
        CHECK (STATUS IN ('ACTIVE', 'EXPIRED', 'CANCELLED')),

    CONSTRAINT CK_SUBSCRIPTION_PRICE
        CHECK (PRICE >= 0)
);



-- 구독 시퀀스 생성
CREATE SEQUENCE SUBSCRIPTION_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999
    CACHE 2
    NOCYCLE;



-- 코멘트
COMMENT ON TABLE SUBSCRIPTION IS 'Premium 구독';

COMMENT ON COLUMN SUBSCRIPTION.SUBSCRIPTION_ID IS '구독 번호';
COMMENT ON COLUMN SUBSCRIPTION.MEMBER_ID IS '회원 번호';
COMMENT ON COLUMN SUBSCRIPTION.PLAN_TYPE IS '구독 플랜(MONTHLY, YEARLY)';
COMMENT ON COLUMN SUBSCRIPTION.STATUS IS '구독 상태(ACTIVE, EXPIRED, CANCELLED)';
COMMENT ON COLUMN SUBSCRIPTION.PRICE IS '구독 결제 금액';
COMMENT ON COLUMN SUBSCRIPTION.START_DATE IS '구독 시작일';
COMMENT ON COLUMN SUBSCRIPTION.END_DATE IS '구독 종료일';
COMMENT ON COLUMN SUBSCRIPTION.CREATED_AT IS '등록일';
COMMENT ON COLUMN SUBSCRIPTION.UPDATED_AT IS '수정일';


COMMIT;



/************************************************/
/* 1. 월간 Premium 테스트 데이터 등록 */
/************************************************/

INSERT INTO SUBSCRIPTION (
    SUBSCRIPTION_ID,
    MEMBER_ID,
    PLAN_TYPE,
    STATUS,
    PRICE,
    START_DATE,
    END_DATE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    SUBSCRIPTION_SEQ.NEXTVAL,
    4,
    'MONTHLY',
    'ACTIVE',
    5000,
    SYSDATE,
    ADD_MONTHS(SYSDATE, 1),
    SYSDATE,
    SYSDATE
);

COMMIT;



/************************************************/
/* 2. 연간 Premium 테스트 데이터 등록 예시 */
/************************************************/

-- 필요할 때만 실행
/*
INSERT INTO SUBSCRIPTION (
    SUBSCRIPTION_ID,
    MEMBER_ID,
    PLAN_TYPE,
    STATUS,
    PRICE,
    START_DATE,
    END_DATE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    SUBSCRIPTION_SEQ.NEXTVAL,
    4,
    'YEARLY',
    'ACTIVE',
    40000,
    SYSDATE,
    ADD_MONTHS(SYSDATE, 12),
    SYSDATE,
    SYSDATE
);

COMMIT;
*/



/************************************************/
/* 3. 전체 구독 조회 */
/************************************************/

SELECT
    SUBSCRIPTION_ID,
    MEMBER_ID,
    PLAN_TYPE,
    STATUS,
    PRICE,
    START_DATE,
    END_DATE,
    CREATED_AT,
    UPDATED_AT
FROM SUBSCRIPTION
ORDER BY SUBSCRIPTION_ID DESC;



/************************************************/
/* 4. 특정 회원 구독 조회 */
/************************************************/

SELECT
    SUBSCRIPTION_ID,
    MEMBER_ID,
    PLAN_TYPE,
    STATUS,
    PRICE,
    START_DATE,
    END_DATE
FROM SUBSCRIPTION
WHERE MEMBER_ID = 4
ORDER BY SUBSCRIPTION_ID DESC;



/************************************************/
/* 5. 특정 회원 현재 Premium 여부 조회 */
/************************************************/

SELECT COUNT(*) AS CNT
FROM SUBSCRIPTION
WHERE MEMBER_ID = 4
  AND STATUS = 'ACTIVE'
  AND START_DATE <= SYSDATE
  AND END_DATE > SYSDATE;



/************************************************/
/* 6. 만료된 구독 EXPIRED 처리 */
/************************************************/

UPDATE SUBSCRIPTION
SET
    STATUS = 'EXPIRED',
    UPDATED_AT = SYSDATE
WHERE STATUS = 'ACTIVE'
  AND END_DATE <= SYSDATE;

COMMIT;



/************************************************/
/* 7. 구독 취소 */
/************************************************/

-- 테스트용
/*
UPDATE SUBSCRIPTION
SET
    STATUS = 'CANCELLED',
    UPDATED_AT = SYSDATE
WHERE SUBSCRIPTION_ID = 1;

COMMIT;
*/



/************************************************/
/* 8. 테이블 생성 여부 확인 */
/************************************************/

SELECT TABLE_NAME
FROM USER_TABLES
WHERE TABLE_NAME = 'SUBSCRIPTION';



/************************************************/
/* 9. 시퀀스 생성 여부 확인 */
/************************************************/

SELECT SEQUENCE_NAME
FROM USER_SEQUENCES
WHERE SEQUENCE_NAME = 'SUBSCRIPTION_SEQ';



/************************************************/
/* 10. 최종 전체 조회 */
/************************************************/

SELECT *
FROM SUBSCRIPTION
ORDER BY SUBSCRIPTION_ID DESC;

DESC SUBSCRIPTION;