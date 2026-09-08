/******************************************************************
Table Name: 회원
******************************************************************/

-- 기존 테이블을 새로 생성해야 하는 경우에만 실행
DROP TABLE MEMBER CASCADE CONSTRAINTS;
DROP SEQUENCE MEMBER_SEQ;


/******************************************************************
1. Sequence
******************************************************************/

CREATE SEQUENCE MEMBER_SEQ
START WITH 1
INCREMENT BY 1
MAXVALUE 9999999
CACHE 2
NOCYCLE;


/******************************************************************
2. MEMBER 테이블 생성
******************************************************************/

CREATE TABLE MEMBER (
    MEMBER_ID      NUMBER         NOT NULL,
    GRADE_ID       NUMBER,
    LOGIN_ID       VARCHAR2(50)   NOT NULL UNIQUE,
    PASSWORD       VARCHAR2(255)  NOT NULL,
    NAME           VARCHAR2(50)   NOT NULL,
    NICKNAME       VARCHAR2(50)   NOT NULL UNIQUE,
    EMAIL          VARCHAR2(100)  NOT NULL UNIQUE,
    TEL            VARCHAR2(20),
    ZIPCODE        VARCHAR2(10),
    ADDRESS1       VARCHAR2(255),
    ADDRESS2       VARCHAR2(255),
    SKIN_TYPE      VARCHAR2(30),
    PERSONAL_COLOR VARCHAR2(30),
    STATUS         VARCHAR2(20)   NOT NULL,
    CREATED_AT     TIMESTAMP      DEFAULT SYSDATE,
    UPDATED_AT     TIMESTAMP,
    WITHDRAWN_AT   TIMESTAMP,

    PRIMARY KEY (MEMBER_ID),

    CONSTRAINT FK_MEMBER_GRADE
        FOREIGN KEY (GRADE_ID)
        REFERENCES GRADE(GRADE_ID)
);


/******************************************************************
3. Comments
******************************************************************/

COMMENT ON TABLE MEMBER
IS '회원';

COMMENT ON COLUMN MEMBER.MEMBER_ID
IS '회원 번호';

COMMENT ON COLUMN MEMBER.GRADE_ID
IS '등급 번호';

COMMENT ON COLUMN MEMBER.LOGIN_ID
IS '로그인 아이디';

COMMENT ON COLUMN MEMBER.PASSWORD
IS '비밀번호';

COMMENT ON COLUMN MEMBER.NAME
IS '회원 이름';

COMMENT ON COLUMN MEMBER.NICKNAME
IS '닉네임';

COMMENT ON COLUMN MEMBER.EMAIL
IS '이메일';

COMMENT ON COLUMN MEMBER.TEL
IS '전화번호';

COMMENT ON COLUMN MEMBER.ZIPCODE
IS '우편번호';

COMMENT ON COLUMN MEMBER.ADDRESS1
IS '기본 주소';

COMMENT ON COLUMN MEMBER.ADDRESS2
IS '상세 주소';

COMMENT ON COLUMN MEMBER.SKIN_TYPE
IS '현재 피부 타입';

COMMENT ON COLUMN MEMBER.PERSONAL_COLOR
IS '현재 퍼스널 컬러';

COMMENT ON COLUMN MEMBER.STATUS
IS '회원 상태';

COMMENT ON COLUMN MEMBER.CREATED_AT
IS '가입일';

COMMENT ON COLUMN MEMBER.UPDATED_AT
IS '수정일';

COMMENT ON COLUMN MEMBER.WITHDRAWN_AT
IS '탈퇴일';

COMMIT;


/******************************************************************
4. CREATE - 회원 등록 테스트
******************************************************************/

-- 일반 회원 USER
INSERT INTO MEMBER (
    MEMBER_ID,
    GRADE_ID,
    LOGIN_ID,
    PASSWORD,
    NAME,
    NICKNAME,
    EMAIL,
    TEL,
    ZIPCODE,
    ADDRESS1,
    ADDRESS2,
    STATUS,
    CREATED_AT
)
VALUES (
    MEMBER_SEQ.NEXTVAL,
    (
        SELECT GRADE_ID
        FROM GRADE
        WHERE NAME = 'USER'
    ),
    'wangnuni',
    '1234',
    '왕눈이',
    '왕눈이짱',
    'wangnuni@test.com',
    '010-1111-1111',
    '12345',
    '서울특별시 종로구',
    '101호',
    'ACTIVE',
    SYSDATE
);


-- GOLD 회원
INSERT INTO MEMBER (
    MEMBER_ID,
    GRADE_ID,
    LOGIN_ID,
    PASSWORD,
    NAME,
    NICKNAME,
    EMAIL,
    TEL,
    ZIPCODE,
    ADDRESS1,
    ADDRESS2,
    STATUS,
    CREATED_AT
)
VALUES (
    MEMBER_SEQ.NEXTVAL,
    (
        SELECT GRADE_ID
        FROM GRADE
        WHERE NAME = 'GOLD'
    ),
    'golduser',
    '1234',
    '골드회원',
    '골드회원',
    'golduser@test.com',
    '010-2222-2222',
    '23456',
    '서울특별시 강남구',
    '202호',
    'ACTIVE',
    SYSDATE
);


-- VIP 회원
INSERT INTO MEMBER (
    MEMBER_ID,
    GRADE_ID,
    LOGIN_ID,
    PASSWORD,
    NAME,
    NICKNAME,
    EMAIL,
    TEL,
    ZIPCODE,
    ADDRESS1,
    ADDRESS2,
    STATUS,
    CREATED_AT
)
VALUES (
    MEMBER_SEQ.NEXTVAL,
    (
        SELECT GRADE_ID
        FROM GRADE
        WHERE NAME = 'VIP'
    ),
    'vipuser',
    '1234',
    'VIP회원',
    'VIP회원',
    'vipuser@test.com',
    '010-3333-3333',
    '34567',
    '서울특별시 송파구',
    '303호',
    'ACTIVE',
    SYSDATE
);


-- 관리자 회원
INSERT INTO MEMBER (
    MEMBER_ID,
    GRADE_ID,
    LOGIN_ID,
    PASSWORD,
    NAME,
    NICKNAME,
    EMAIL,
    TEL,
    ZIPCODE,
    ADDRESS1,
    ADDRESS2,
    STATUS,
    CREATED_AT
)
VALUES (
    MEMBER_SEQ.NEXTVAL,
    (
        SELECT GRADE_ID
        FROM GRADE
        WHERE NAME = 'ADMIN'
    ),
    'admin',
    '1234',
    '관리자',
    '관리자',
    'admin@onniver.com',
    '010-4444-4444',
    '45678',
    '서울특별시 서초구',
    '404호',
    'ACTIVE',
    SYSDATE
);


-- DELETE 테스트용 회원
INSERT INTO MEMBER (
    MEMBER_ID,
    GRADE_ID,
    LOGIN_ID,
    PASSWORD,
    NAME,
    NICKNAME,
    EMAIL,
    TEL,
    ZIPCODE,
    ADDRESS1,
    ADDRESS2,
    STATUS,
    CREATED_AT
)
VALUES (
    MEMBER_SEQ.NEXTVAL,
    (
        SELECT GRADE_ID
        FROM GRADE
        WHERE NAME = 'USER'
    ),
    'delete_test',
    '1234',
    '삭제테스트',
    '삭제용',
    'delete@test.com',
    '010-5555-5555',
    '56789',
    '경기도 성남시',
    '505호',
    'ACTIVE',
    SYSDATE
);

COMMIT;


/******************************************************************
5. READ - 전체 회원 조회
******************************************************************/

SELECT
    MEMBER_ID,
    GRADE_ID,
    LOGIN_ID,
    PASSWORD,
    NAME,
    NICKNAME,
    EMAIL,
    TEL,
    ZIPCODE,
    ADDRESS1,
    ADDRESS2,
    STATUS,
    CREATED_AT,
    UPDATED_AT,
    WITHDRAWN_AT
FROM MEMBER
ORDER BY MEMBER_ID ASC;


/******************************************************************
6. JOIN - MEMBER + GRADE 회원 등급 포함 전체 조회
******************************************************************/

/*
 * MEMBER 테이블에는 GRADE_ID만 저장되어 있으므로
 * GRADE 테이블과 GRADE_ID를 기준으로 JOIN하여
 * 회원 정보와 실제 등급명(USER, GOLD, VIP, ADMIN)을 함께 조회한다.
 *
 * MEMBER.GRADE_ID = GRADE.GRADE_ID
 */

SELECT
    M.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME,
    M.EMAIL,
    M.TEL,

    -- MEMBER가 가지고 있는 등급 번호
    M.GRADE_ID,

    -- GRADE 테이블에서 실제 등급명 조회
    G.NAME AS GRADE_NAME,

    M.STATUS,
    M.CREATED_AT,
    M.UPDATED_AT,
    M.WITHDRAWN_AT

FROM MEMBER M

LEFT JOIN GRADE G
    ON M.GRADE_ID = G.GRADE_ID

ORDER BY M.MEMBER_ID ASC;


/******************************************************************
7. READ - 회원 번호 조회
******************************************************************/

SELECT
    MEMBER_ID,
    GRADE_ID,
    LOGIN_ID,
    NAME,
    NICKNAME,
    EMAIL,
    TEL,
    ZIPCODE,
    ADDRESS1,
    ADDRESS2,
    STATUS,
    CREATED_AT,
    UPDATED_AT,
    WITHDRAWN_AT
FROM MEMBER
WHERE MEMBER_ID = 1;


/******************************************************************
8. READ - LOGIN_ID 회원 조회
******************************************************************/

SELECT *
FROM MEMBER
WHERE LOGIN_ID = 'wangnuni';


/******************************************************************
9. JOIN - 회원 등급별 조회
******************************************************************/

-- USER 등급의 회원만 조회
SELECT
    M.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME,
    M.EMAIL,
    M.GRADE_ID,
    G.NAME AS GRADE_NAME,
    M.STATUS
FROM MEMBER M
JOIN GRADE G
    ON M.GRADE_ID = G.GRADE_ID
WHERE G.NAME = 'USER'
ORDER BY M.MEMBER_ID ASC;


/******************************************************************
10. COUNT - 전체 회원 수
******************************************************************/

SELECT COUNT(*) AS CNT
FROM MEMBER;


/******************************************************************
11. JOIN + COUNT - 등급별 회원 수
******************************************************************/

SELECT
    G.GRADE_ID,
    G.NAME AS GRADE_NAME,
    COUNT(M.MEMBER_ID) AS CNT
FROM GRADE G
LEFT JOIN MEMBER M
    ON G.GRADE_ID = M.GRADE_ID
GROUP BY
    G.GRADE_ID,
    G.NAME
ORDER BY G.GRADE_ID ASC;


/******************************************************************
12. COUNT - 아이디 중복 검사
******************************************************************/

SELECT COUNT(*) AS CNT
FROM MEMBER
WHERE LOGIN_ID = 'wangnuni';


/******************************************************************
13. COUNT - 이메일 중복 검사
******************************************************************/

SELECT COUNT(*) AS CNT
FROM MEMBER
WHERE EMAIL = 'wangnuni@test.com';


/******************************************************************
14. COUNT - 닉네임 중복 검사
******************************************************************/

SELECT COUNT(*) AS CNT
FROM MEMBER
WHERE NICKNAME = '왕눈이짱';


/******************************************************************
15. UPDATE - 회원 정보 수정
******************************************************************/

UPDATE MEMBER
SET
    NICKNAME = '왕눈이최고',
    EMAIL = 'wangnuni2@test.com',
    TEL = '010-9999-9999',
    UPDATED_AT = SYSDATE
WHERE LOGIN_ID = 'wangnuni';

COMMIT;


/******************************************************************
16. UPDATE - 비밀번호 수정
******************************************************************/

UPDATE MEMBER
SET
    PASSWORD = '5678',
    UPDATED_AT = SYSDATE
WHERE LOGIN_ID = 'golduser';

COMMIT;


/******************************************************************
17. UPDATE - 회원 등급 수정 테스트
******************************************************************/

-- USER 회원을 GOLD 회원으로 변경 테스트
UPDATE MEMBER
SET
    GRADE_ID = (
        SELECT GRADE_ID
        FROM GRADE
        WHERE NAME = 'GOLD'
    ),
    UPDATED_AT = SYSDATE
WHERE LOGIN_ID = 'wangnuni';

COMMIT;


/******************************************************************
18. UPDATE - 회원 탈퇴 처리
******************************************************************/

UPDATE MEMBER
SET
    STATUS = 'WITHDRAWN',
    WITHDRAWN_AT = SYSDATE,
    UPDATED_AT = SYSDATE
WHERE LOGIN_ID = 'golduser';

COMMIT;


/******************************************************************
19. DELETE - 회원 삭제 테스트
******************************************************************/

DELETE FROM MEMBER
WHERE LOGIN_ID = 'delete_test';

COMMIT;


/******************************************************************
20. JOIN - 최종 회원 + 등급 전체 조회
******************************************************************/

SELECT
    M.MEMBER_ID,
    M.GRADE_ID,
    G.NAME AS GRADE_NAME,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME,
    M.EMAIL,
    M.TEL,
    M.ZIPCODE,
    M.ADDRESS1,
    M.ADDRESS2,
    M.STATUS,
    M.CREATED_AT,
    M.UPDATED_AT,
    M.WITHDRAWN_AT
FROM MEMBER M
LEFT JOIN GRADE G
    ON M.GRADE_ID = G.GRADE_ID
ORDER BY M.MEMBER_ID ASC;


/******************************************************************
21. 회원 탈퇴 상태 확인
******************************************************************/

SELECT
    MEMBER_ID,
    LOGIN_ID,
    STATUS,
    WITHDRAWN_AT
FROM MEMBER
WHERE LOGIN_ID = 'delete_test';


/******************************************************************
22. 관리자 회원 확인
******************************************************************/

SELECT
    MEMBER_ID,
    LOGIN_ID,
    PASSWORD,
    NAME,
    EMAIL
FROM MEMBER
WHERE LOGIN_ID = 'admin1';


/******************************************************************
23. 전화번호 하이픈 형식 일괄 변경
******************************************************************/

UPDATE MEMBER
SET TEL =
    SUBSTR(TEL, 1, 3)
    || '-'
    || SUBSTR(TEL, 4, 4)
    || '-'
    || SUBSTR(TEL, 8, 4)
WHERE REGEXP_LIKE(
    TEL,
    '^[0-9]{11}$'
);

COMMIT;



/******************************************************************
JOIN 실행문
*******************************************************************/

SELECT
    M.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME,
    M.GRADE_ID,
    G.NAME AS GRADE_NAME,
    M.STATUS
FROM MEMBER M
LEFT JOIN GRADE G
    ON M.GRADE_ID = G.GRADE_ID
ORDER BY M.MEMBER_ID ASC;

ALTER TABLE MEMBER
ADD (
    SKIN_TYPE VARCHAR2(30),
    PERSONAL_COLOR VARCHAR2(30)
);

COMMENT ON COLUMN MEMBER.SKIN_TYPE
IS '현재 피부 타입';

COMMENT ON COLUMN MEMBER.PERSONAL_COLOR
IS '현재 퍼스널 컬러';

COMMIT;

SELECT
    MEMBER_ID,
    LOGIN_ID,
    NAME,
    SKIN_TYPE,
    PERSONAL_COLOR
FROM MEMBER
ORDER BY MEMBER_ID;