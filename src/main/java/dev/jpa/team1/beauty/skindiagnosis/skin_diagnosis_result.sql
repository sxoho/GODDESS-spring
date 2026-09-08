/******************************************************************
Table Name: 피부 타입 진단 결과
******************************************************************/

-- 기존 테이블을 새로 생성해야 하는 경우에만 실행
DROP TABLE SKIN_DIAGNOSIS_RESULT CASCADE CONSTRAINTS;

DROP SEQUENCE SKIN_DIAGNOSIS_RESULT_SEQ;


/******************************************************************
1. Sequence
******************************************************************/

CREATE SEQUENCE SKIN_DIAGNOSIS_RESULT_SEQ
START WITH 1
INCREMENT BY 1
MAXVALUE 9999999
CACHE 2
NOCYCLE;


/******************************************************************
2. SKIN_DIAGNOSIS_RESULT 테이블 생성
******************************************************************/

CREATE TABLE SKIN_DIAGNOSIS_RESULT (

    SKINDIAGNOSIS_ID   NUMBER         NOT NULL,

    MEMBER_ID           NUMBER         NOT NULL,

    SKIN_TYPE           VARCHAR2(30)   NOT NULL,

    DRY_SCORE           NUMBER         DEFAULT 0 NOT NULL,

    OILY_SCORE          NUMBER         DEFAULT 0 NOT NULL,

    COMBINATION_SCORE   NUMBER         DEFAULT 0 NOT NULL,

    NORMAL_SCORE        NUMBER         DEFAULT 0 NOT NULL,

    SENSITIVE_SCORE     NUMBER         DEFAULT 0 NOT NULL,

    DEHYDRATED_SCORE    NUMBER         DEFAULT 0 NOT NULL,

    RESULT_DESCRIPTION  VARCHAR2(1000),

    CREATED_AT          TIMESTAMP      DEFAULT SYSDATE NOT NULL,

    PRIMARY KEY (
        SKINDIAGNOSIS_ID
    ),

    CONSTRAINT FK_SKIN_DIAGNOSIS_MEMBER
        FOREIGN KEY (
            MEMBER_ID
        )
        REFERENCES MEMBER(
            MEMBER_ID
        )
);


/******************************************************************
3. Comments
******************************************************************/

COMMENT ON TABLE SKIN_DIAGNOSIS_RESULT
IS '피부 타입 자가진단 결과';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.SKINDIAGNOSIS_ID
IS '피부 진단 결과 번호';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.MEMBER_ID
IS '회원 번호';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.SKIN_TYPE
IS '최종 피부 타입';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.DRY_SCORE
IS '건성 점수';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.OILY_SCORE
IS '지성 점수';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.COMBINATION_SCORE
IS '복합성 점수';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.NORMAL_SCORE
IS '중성 점수';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.SENSITIVE_SCORE
IS '민감성 점수';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.DEHYDRATED_SCORE
IS '수부지 점수';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.RESULT_DESCRIPTION
IS '진단 결과 설명';


COMMENT ON COLUMN SKIN_DIAGNOSIS_RESULT.CREATED_AT
IS '진단 일시';


COMMIT;


/******************************************************************
4. CREATE - 피부 진단 결과 등록 테스트
******************************************************************/

-- 건성 진단 결과
INSERT INTO SKIN_DIAGNOSIS_RESULT (

    SKINDIAGNOSIS_ID,

    MEMBER_ID,

    SKIN_TYPE,

    DRY_SCORE,

    OILY_SCORE,

    COMBINATION_SCORE,

    NORMAL_SCORE,

    SENSITIVE_SCORE,

    DEHYDRATED_SCORE,

    RESULT_DESCRIPTION,

    CREATED_AT

)
VALUES (

    SKIN_DIAGNOSIS_RESULT_SEQ.NEXTVAL,

    (
        SELECT MEMBER_ID
        FROM MEMBER
        WHERE LOGIN_ID = 'wangnuni'
    ),

    'DRY',

    15,

    2,

    3,

    1,

    4,

    2,

    '수분과 유분이 부족해 건조함과 당김이 나타나는 피부 타입입니다.',

    SYSDATE
);


-- 지성 진단 결과
INSERT INTO SKIN_DIAGNOSIS_RESULT (

    SKINDIAGNOSIS_ID,

    MEMBER_ID,

    SKIN_TYPE,

    DRY_SCORE,

    OILY_SCORE,

    COMBINATION_SCORE,

    NORMAL_SCORE,

    SENSITIVE_SCORE,

    DEHYDRATED_SCORE,

    RESULT_DESCRIPTION,

    CREATED_AT

)
VALUES (

    SKIN_DIAGNOSIS_RESULT_SEQ.NEXTVAL,

    (
        SELECT MEMBER_ID
        FROM MEMBER
        WHERE LOGIN_ID = 'golduser'
    ),

    'OILY',

    2,

    16,

    5,

    1,

    2,

    4,

    '피지 분비가 활발해 유분과 번들거림이 쉽게 나타나는 피부 타입입니다.',

    SYSDATE
);


-- 수부지 진단 결과
INSERT INTO SKIN_DIAGNOSIS_RESULT (

    SKINDIAGNOSIS_ID,

    MEMBER_ID,

    SKIN_TYPE,

    DRY_SCORE,

    OILY_SCORE,

    COMBINATION_SCORE,

    NORMAL_SCORE,

    SENSITIVE_SCORE,

    DEHYDRATED_SCORE,

    RESULT_DESCRIPTION,

    CREATED_AT

)
VALUES (

    SKIN_DIAGNOSIS_RESULT_SEQ.NEXTVAL,

    (
        SELECT MEMBER_ID
        FROM MEMBER
        WHERE LOGIN_ID = 'admin'
    ),

    'DEHYDRATED',

    5,

    7,

    4,

    1,

    3,

    18,

    '겉에는 유분이 느껴지지만 피부 속 수분이 부족한 수부지 피부 타입입니다.',

    SYSDATE
);


COMMIT;


/******************************************************************
5. READ - 전체 피부 진단 결과 조회
******************************************************************/

SELECT

    SKINDIAGNOSIS_ID,

    MEMBER_ID,

    SKIN_TYPE,

    DRY_SCORE,

    OILY_SCORE,

    COMBINATION_SCORE,

    NORMAL_SCORE,

    SENSITIVE_SCORE,

    DEHYDRATED_SCORE,

    RESULT_DESCRIPTION,

    CREATED_AT

FROM SKIN_DIAGNOSIS_RESULT

ORDER BY SKINDIAGNOSIS_ID ASC;


/******************************************************************
6. READ - 진단 결과 번호 조회
******************************************************************/

SELECT

    SKINDIAGNOSIS_ID,

    MEMBER_ID,

    SKIN_TYPE,

    DRY_SCORE,

    OILY_SCORE,

    COMBINATION_SCORE,

    NORMAL_SCORE,

    SENSITIVE_SCORE,

    DEHYDRATED_SCORE,

    RESULT_DESCRIPTION,

    CREATED_AT

FROM SKIN_DIAGNOSIS_RESULT

WHERE SKINDIAGNOSIS_ID = 1;


/******************************************************************
7. READ - 회원별 피부 진단 이력 조회
******************************************************************/

SELECT

    SKINDIAGNOSIS_ID,

    MEMBER_ID,

    SKIN_TYPE,

    DRY_SCORE,

    OILY_SCORE,

    COMBINATION_SCORE,

    NORMAL_SCORE,

    SENSITIVE_SCORE,

    DEHYDRATED_SCORE,

    RESULT_DESCRIPTION,

    CREATED_AT

FROM SKIN_DIAGNOSIS_RESULT

WHERE MEMBER_ID = (
    SELECT MEMBER_ID
    FROM MEMBER
    WHERE LOGIN_ID = 'wangnuni'
)

ORDER BY CREATED_AT DESC;


/******************************************************************
8. JOIN - 피부 진단 결과 + 회원 정보 조회
******************************************************************/

SELECT

    S.SKINDIAGNOSIS_ID,

    S.MEMBER_ID,

    M.LOGIN_ID,

    M.NAME,

    M.NICKNAME,

    S.SKIN_TYPE,

    S.DRY_SCORE,

    S.OILY_SCORE,

    S.COMBINATION_SCORE,

    S.NORMAL_SCORE,

    S.SENSITIVE_SCORE,

    S.DEHYDRATED_SCORE,

    S.RESULT_DESCRIPTION,

    S.CREATED_AT

FROM SKIN_DIAGNOSIS_RESULT S

JOIN MEMBER M
    ON S.MEMBER_ID = M.MEMBER_ID

ORDER BY S.CREATED_AT DESC;


/******************************************************************
9. JOIN - 회원 현재 피부 타입 + 진단 결과 조회
******************************************************************/

SELECT

    M.MEMBER_ID,

    M.LOGIN_ID,

    M.NAME,

    M.SKIN_TYPE AS MEMBER_SKIN_TYPE,

    S.SKIN_TYPE AS DIAGNOSIS_SKIN_TYPE,

    S.CREATED_AT

FROM MEMBER M

LEFT JOIN SKIN_DIAGNOSIS_RESULT S
    ON M.MEMBER_ID = S.MEMBER_ID

ORDER BY
    M.MEMBER_ID ASC,
    S.CREATED_AT DESC;


/******************************************************************
10. READ - 회원의 가장 최근 피부 진단 결과 조회
******************************************************************/

SELECT *

FROM (

    SELECT

        S.SKINDIAGNOSIS_ID,

        S.MEMBER_ID,

        S.SKIN_TYPE,

        S.DRY_SCORE,

        S.OILY_SCORE,

        S.COMBINATION_SCORE,

        S.NORMAL_SCORE,

        S.SENSITIVE_SCORE,

        S.DEHYDRATED_SCORE,

        S.RESULT_DESCRIPTION,

        S.CREATED_AT

    FROM SKIN_DIAGNOSIS_RESULT S

    WHERE S.MEMBER_ID = (
        SELECT MEMBER_ID
        FROM MEMBER
        WHERE LOGIN_ID = 'admin'
    )

    ORDER BY S.CREATED_AT DESC

)

WHERE ROWNUM = 1;


/******************************************************************
11. COUNT - 전체 피부 진단 결과 수
******************************************************************/

SELECT COUNT(*) AS CNT

FROM SKIN_DIAGNOSIS_RESULT;


/******************************************************************
12. COUNT - 회원별 피부 진단 횟수
******************************************************************/

SELECT

    MEMBER_ID,

    COUNT(*) AS DIAGNOSIS_COUNT

FROM SKIN_DIAGNOSIS_RESULT

GROUP BY MEMBER_ID

ORDER BY MEMBER_ID ASC;


/******************************************************************
13. COUNT - 피부 타입별 진단 결과 수
******************************************************************/

SELECT

    SKIN_TYPE,

    COUNT(*) AS CNT

FROM SKIN_DIAGNOSIS_RESULT

GROUP BY SKIN_TYPE

ORDER BY SKIN_TYPE ASC;


/******************************************************************
14. UPDATE - 피부 진단 결과 수정 테스트
******************************************************************/

UPDATE SKIN_DIAGNOSIS_RESULT

SET

    SKIN_TYPE = 'COMBINATION',

    DRY_SCORE = 5,

    OILY_SCORE = 5,

    COMBINATION_SCORE = 15,

    NORMAL_SCORE = 2,

    SENSITIVE_SCORE = 3,

    DEHYDRATED_SCORE = 6,

    RESULT_DESCRIPTION =
        '부위에 따라 유분과 건조함이 동시에 나타나는 복합성 피부 타입입니다.'

WHERE SKINDIAGNOSIS_ID = 1;


COMMIT;


/******************************************************************
15. UPDATE - 회원 현재 피부 타입 변경 테스트
******************************************************************/

UPDATE MEMBER

SET

    SKIN_TYPE = 'DEHYDRATED',

    UPDATED_AT = SYSDATE

WHERE LOGIN_ID = 'admin';


COMMIT;


/******************************************************************
16. DELETE - 피부 진단 결과 삭제 테스트
******************************************************************/

DELETE FROM SKIN_DIAGNOSIS_RESULT

WHERE SKINDIAGNOSIS_ID = 1;


COMMIT;


/******************************************************************
17. DELETE - 특정 회원 피부 진단 이력 삭제 테스트
******************************************************************/

DELETE FROM SKIN_DIAGNOSIS_RESULT

WHERE MEMBER_ID = (
    SELECT MEMBER_ID
    FROM MEMBER
    WHERE LOGIN_ID = 'delete_test'
);


COMMIT;


/******************************************************************
18. 최종 조회 - 피부 진단 결과 전체
******************************************************************/

SELECT

    S.SKINDIAGNOSIS_ID,

    M.LOGIN_ID,

    M.NAME,

    S.SKIN_TYPE,

    S.DRY_SCORE,

    S.OILY_SCORE,

    S.COMBINATION_SCORE,

    S.NORMAL_SCORE,

    S.SENSITIVE_SCORE,

    S.DEHYDRATED_SCORE,

    S.RESULT_DESCRIPTION,

    S.CREATED_AT

FROM SKIN_DIAGNOSIS_RESULT S

JOIN MEMBER M
    ON S.MEMBER_ID = M.MEMBER_ID

ORDER BY S.CREATED_AT DESC;