/******************************************************************
Table Name: 퍼스널컬러 진단 결과
Physical Name: PERSONAL_COLOR_RESULT
******************************************************************/

/*
중요:
PERSONAL_COLOR_RESULT 테이블과
PERSONAL_COLOR_RESULT_SEQ 시퀀스가 이미 존재한다면
아래 DROP / CREATE 부분은 실행하지 않습니다.

기존 데이터가 있는 상태에서 DROP 하면 데이터가 삭제됩니다.
*/


/******************************************************************
0. 기존 객체 확인
******************************************************************/

SELECT TABLE_NAME
FROM USER_TABLES
WHERE TABLE_NAME = 'PERSONAL_COLOR_RESULT';


SELECT SEQUENCE_NAME
FROM USER_SEQUENCES
WHERE SEQUENCE_NAME = 'PERSONAL_COLOR_RESULT_SEQ';


/******************************************************************
1. 기존 객체 삭제
신규로 다시 생성할 때만 실행
******************************************************************/

/*
DROP TABLE PERSONAL_COLOR_RESULT CASCADE CONSTRAINTS;

DROP SEQUENCE PERSONAL_COLOR_RESULT_SEQ;
*/


/******************************************************************
2. Sequence
******************************************************************/

CREATE SEQUENCE PERSONAL_COLOR_RESULT_SEQ
START WITH 1
INCREMENT BY 1
MAXVALUE 9999999
CACHE 2
NOCYCLE;


/******************************************************************
3. PERSONAL_COLOR_RESULT 테이블 생성
ERD 컬럼명 기준
******************************************************************/

CREATE TABLE PERSONAL_COLOR_RESULT (

    PERSONALCOLOR_ID
        NUMBER
        NOT NULL,

    MEMBER_ID
        NUMBER
        NOT NULL,

    PERSONAL_COLOR
        VARCHAR2(30)
        NOT NULL,

    SPRING_SCORE
        NUMBER
        DEFAULT 0
        NOT NULL,

    SUMMER_SCORE
        NUMBER
        DEFAULT 0
        NOT NULL,

    AUTUMN_SCORE
        NUMBER
        DEFAULT 0
        NOT NULL,

    WINTER_SCORE
        NUMBER
        DEFAULT 0
        NOT NULL,

    RESULT_DESCRIPTION
        VARCHAR2(1000),

    CREATED_AT
        DATE
        DEFAULT SYSDATE
        NOT NULL,


    PRIMARY KEY (
        PERSONALCOLOR_ID
    ),


    CONSTRAINT FK_PERSONAL_COLOR_MEMBER
        FOREIGN KEY (
            MEMBER_ID
        )
        REFERENCES MEMBER(
            MEMBER_ID
        ),


    CONSTRAINT CK_PERSONAL_COLOR_TYPE
        CHECK (
            PERSONAL_COLOR IN (
                'SPRING_WARM',
                'SUMMER_COOL',
                'AUTUMN_WARM',
                'WINTER_COOL'
            )
        )
);


/******************************************************************
4. Comments
******************************************************************/

COMMENT ON TABLE PERSONAL_COLOR_RESULT
IS '퍼스널컬러 자가진단 결과';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.PERSONALCOLOR_ID
IS '퍼스널컬러 진단 번호';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.MEMBER_ID
IS '회원 번호';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.PERSONAL_COLOR
IS '최종 퍼스널컬러';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.SPRING_SCORE
IS '봄 웜톤 점수';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.SUMMER_SCORE
IS '여름 쿨톤 점수';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.AUTUMN_SCORE
IS '가을 웜톤 점수';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.WINTER_SCORE
IS '겨울 쿨톤 점수';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.RESULT_DESCRIPTION
IS '진단 결과 설명';


COMMENT ON COLUMN PERSONAL_COLOR_RESULT.CREATED_AT
IS '진단일';


COMMIT;


/******************************************************************
5. 전체 조회
******************************************************************/

SELECT *
FROM PERSONAL_COLOR_RESULT
ORDER BY PERSONALCOLOR_ID DESC;


/******************************************************************
6. 특정 회원 진단 이력 조회
******************************************************************/

SELECT *
FROM PERSONAL_COLOR_RESULT
WHERE MEMBER_ID = 4
ORDER BY CREATED_AT DESC;


/******************************************************************
7. MEMBER와 JOIN 조회
******************************************************************/

SELECT
    P.PERSONALCOLOR_ID,
    P.MEMBER_ID,
    M.LOGIN_ID,
    M.NICKNAME,
    P.PERSONAL_COLOR,
    P.SPRING_SCORE,
    P.SUMMER_SCORE,
    P.AUTUMN_SCORE,
    P.WINTER_SCORE,
    P.RESULT_DESCRIPTION,
    P.CREATED_AT
FROM PERSONAL_COLOR_RESULT P
JOIN MEMBER M
    ON P.MEMBER_ID = M.MEMBER_ID
ORDER BY P.CREATED_AT DESC;


/******************************************************************
8. MEMBER 현재 퍼스널컬러 확인
******************************************************************/

SELECT
    MEMBER_ID,
    LOGIN_ID,
    PERSONAL_COLOR
FROM MEMBER
WHERE MEMBER_ID = 4;


/******************************************************************
9. 데이터 개수
******************************************************************/

SELECT COUNT(*)
FROM PERSONAL_COLOR_RESULT;


/******************************************************************
10. 최종 확인
******************************************************************/

SELECT
    PERSONALCOLOR_ID,
    MEMBER_ID,
    PERSONAL_COLOR,
    SPRING_SCORE,
    SUMMER_SCORE,
    AUTUMN_SCORE,
    WINTER_SCORE,
    RESULT_DESCRIPTION,
    CREATED_AT
FROM PERSONAL_COLOR_RESULT
ORDER BY PERSONALCOLOR_ID DESC;