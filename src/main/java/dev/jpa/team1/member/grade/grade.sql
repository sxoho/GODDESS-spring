-- SQL을 직접 적용하는 경우 테스트
-- 회원 등급 테이블 및 CRUD 테스트 SQL

/******************************************************************
************************ Table Name: 회원 등급 *********************
******************************************************************/

-- 기존 테이블 삭제
DROP TABLE GRADE CASCADE CONSTRAINTS;

-- 기존 시퀀스 삭제
DROP SEQUENCE GRADE_SEQ;


/******************************************************************
1. 시퀀스 생성
******************************************************************/

-- 1 ~ 4번은 기본 등급 번호로 직접 사용
-- 이후 새 등급 등록 시 5번부터 시작
CREATE SEQUENCE GRADE_SEQ
START WITH 5
INCREMENT BY 1
MAXVALUE 99999
CACHE 2
NOCYCLE;


/******************************************************************
2. GRADE 테이블 생성
******************************************************************/

CREATE TABLE GRADE (
    GRADE_ID     NUMBER         NOT NULL,
    NAME         VARCHAR2(50)   NOT NULL UNIQUE,
    DESCRIPTION  VARCHAR2(255),
    VISIBLE      VARCHAR2(1)    DEFAULT 'Y' NOT NULL,
    CREATED_AT   TIMESTAMP      DEFAULT SYSDATE NOT NULL,

    PRIMARY KEY (GRADE_ID),

    CONSTRAINT CK_GRADE_VISIBLE
        CHECK (VISIBLE IN ('Y', 'N'))
);


/******************************************************************
3. 테이블 및 컬럼 코멘트
******************************************************************/

COMMENT ON TABLE GRADE IS '회원 등급';

COMMENT ON COLUMN GRADE.GRADE_ID IS '등급 번호';
COMMENT ON COLUMN GRADE.NAME IS '등급명';
COMMENT ON COLUMN GRADE.DESCRIPTION IS '등급 설명';
COMMENT ON COLUMN GRADE.VISIBLE IS '사용 여부 (Y/N)';
COMMENT ON COLUMN GRADE.CREATED_AT IS '등록일';


/******************************************************************
4. 등록 CREATE

등급 기준
1 : USER
2 : GOLD
3 : VIP
4 : ADMIN
******************************************************************/

-- 일반 회원 등급
INSERT INTO GRADE (
    GRADE_ID,
    NAME,
    DESCRIPTION,
    VISIBLE,
    CREATED_AT
)
VALUES (
    1,
    'USER',
    '일반 회원',
    'Y',
    SYSDATE
);


-- GOLD 회원 등급
INSERT INTO GRADE (
    GRADE_ID,
    NAME,
    DESCRIPTION,
    VISIBLE,
    CREATED_AT
)
VALUES (
    2,
    'GOLD',
    '우수 회원',
    'Y',
    SYSDATE
);


-- VIP 회원 등급
INSERT INTO GRADE (
    GRADE_ID,
    NAME,
    DESCRIPTION,
    VISIBLE,
    CREATED_AT
)
VALUES (
    3,
    'VIP',
    '최우수 회원',
    'Y',
    SYSDATE
);


-- 관리자 등급
INSERT INTO GRADE (
    GRADE_ID,
    NAME,
    DESCRIPTION,
    VISIBLE,
    CREATED_AT
)
VALUES (
    4,
    'ADMIN',
    '관리자',
    'Y',
    SYSDATE
);

COMMIT;


/******************************************************************
5. 전체 목록 조회 READ
******************************************************************/

SELECT
    GRADE_ID,
    NAME,
    DESCRIPTION,
    VISIBLE,
    CREATED_AT
FROM GRADE
ORDER BY GRADE_ID ASC;


/******************************************************************
6. 전체 데이터 간단 조회
******************************************************************/

SELECT *
FROM GRADE
ORDER BY GRADE_ID ASC;


/******************************************************************
7. 등급 번호를 이용한 상세 조회
******************************************************************/

SELECT
    GRADE_ID,
    NAME,
    DESCRIPTION,
    VISIBLE,
    CREATED_AT
FROM GRADE
WHERE GRADE_ID = 1;


/******************************************************************
8. 일반 회원 등급 조회
******************************************************************/

SELECT *
FROM GRADE
WHERE GRADE_ID = 1;


/******************************************************************
9. GOLD 등급 조회
******************************************************************/

SELECT *
FROM GRADE
WHERE GRADE_ID = 2;


/******************************************************************
10. VIP 등급 조회
******************************************************************/

SELECT *
FROM GRADE
WHERE GRADE_ID = 3;


/******************************************************************
11. 관리자 등급 조회
******************************************************************/

SELECT *
FROM GRADE
WHERE GRADE_ID = 4;


/******************************************************************
12. 사용 중인 등급 목록 조회
******************************************************************/

SELECT
    GRADE_ID,
    NAME,
    DESCRIPTION,
    VISIBLE,
    CREATED_AT
FROM GRADE
WHERE VISIBLE = 'Y'
ORDER BY GRADE_ID ASC;


/******************************************************************
13. 전체 등급 개수 COUNT
******************************************************************/

SELECT COUNT(*) AS CNT
FROM GRADE;