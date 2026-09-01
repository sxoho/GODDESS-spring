-- SQL을 직접 적용하는 경우 테스트
/**********************************/
/* Table Name: 회원 사진 */
/**********************************/

-- 기존 테이블 삭제
DROP TABLE MEMBER_PHOTO CASCADE CONSTRAINTS;

-- 기존 시퀀스 삭제
DROP SEQUENCE MEMBER_PHOTO_SEQ;


-- 회원 사진 테이블 생성
CREATE TABLE MEMBER_PHOTO (
    member_photo_id NUMBER PRIMARY KEY,              -- 회원 사진 번호
    MEMBER_ID      NUMBER NOT NULL,                 -- 회원 번호
    PHOTO_TYPE     VARCHAR2(50) NOT NULL,           -- 사진 종류
    FILE_ORIGIN    VARCHAR2(255) NOT NULL,          -- 원본 파일명
    FILE_SAVED     VARCHAR2(255) NOT NULL,          -- 저장 파일명
    FILE_PATH      VARCHAR2(500) NOT NULL,          -- 저장 경로
    FILE_SIZE      NUMBER,                          -- 파일 크기
    IS_MAIN        VARCHAR2(1) DEFAULT 'Y' NOT NULL,-- 대표 사진 여부
    VISIBLE        VARCHAR2(1) DEFAULT 'Y' NOT NULL,-- 사용 여부
    CREATED_AT     TIMESTAMP DEFAULT SYSDATE NOT NULL,
    UPDATED_AT     TIMESTAMP,

    CONSTRAINT FK_MEMBER_PHOTO_MEMBER
        FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER(MEMBER_ID),

    CONSTRAINT CK_MEMBER_PHOTO_MAIN
        CHECK (IS_MAIN IN ('Y', 'N')),

    CONSTRAINT CK_MEMBER_PHOTO_VISIBLE
        CHECK (VISIBLE IN ('Y', 'N'))
);


-- 회원 사진 시퀀스 생성
CREATE SEQUENCE MEMBER_PHOTO_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999
    CACHE 2
    NOCYCLE;


-- 코멘트
COMMENT ON TABLE MEMBER_PHOTO IS '회원 사진';

COMMENT ON COLUMN MEMBER_PHOTO.member_photo_id IS '회원 사진 번호';
COMMENT ON COLUMN MEMBER_PHOTO.MEMBER_ID IS '회원 번호';
COMMENT ON COLUMN MEMBER_PHOTO.PHOTO_TYPE IS '사진 종류(FACE, UPPER_BODY, FULL_BODY)';
COMMENT ON COLUMN MEMBER_PHOTO.FILE_ORIGIN IS '원본 파일명';
COMMENT ON COLUMN MEMBER_PHOTO.FILE_SAVED IS '저장 파일명';
COMMENT ON COLUMN MEMBER_PHOTO.FILE_PATH IS '저장 경로';
COMMENT ON COLUMN MEMBER_PHOTO.FILE_SIZE IS '파일 크기';
COMMENT ON COLUMN MEMBER_PHOTO.IS_MAIN IS '대표 사진 여부(Y/N)';
COMMENT ON COLUMN MEMBER_PHOTO.VISIBLE IS '사용 여부(Y/N)';
COMMENT ON COLUMN MEMBER_PHOTO.CREATED_AT IS '등록일';
COMMENT ON COLUMN MEMBER_PHOTO.UPDATED_AT IS '수정일';

COMMIT;



/**********************************/
/* 1. 테스트 데이터 등록 */
/**********************************/

-- 아래 MEMBER_ID는 실제 MEMBER 테이블에 존재하는 값으로 테스트해야 함

INSERT INTO MEMBER_PHOTO (
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    MEMBER_PHOTO_SEQ.NEXTVAL,
    1,
    'FULL_BODY',
    'member1.jpg',
    'member1_20260813.jpg',
    '/member/photo/member1_20260813.jpg',
    245810,
    'Y',
    'Y',
    SYSDATE,
    SYSDATE
);


INSERT INTO MEMBER_PHOTO (
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    MEMBER_PHOTO_SEQ.NEXTVAL,
    2,
    'FULL_BODY',
    'member2.png',
    'member2_20260813.png',
    '/member/photo/member2_20260813.png',
    351200,
    'Y',
    'Y',
    SYSDATE,
    SYSDATE
);


COMMIT;



/**********************************/
/* 2. 전체 회원 사진 조회 */
/**********************************/

SELECT
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM MEMBER_PHOTO
ORDER BY member_photo_id ASC;



/**********************************/
/* 3. 특정 회원의 사진 조회 */
/**********************************/

SELECT
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM MEMBER_PHOTO
WHERE MEMBER_ID = 1
ORDER BY CREATED_AT DESC;



/**********************************/
/* 4. 특정 회원 대표 사진 조회 */
/**********************************/

SELECT
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM MEMBER_PHOTO
WHERE MEMBER_ID = 1
  AND IS_MAIN = 'Y'
  AND VISIBLE = 'Y'
ORDER BY CREATED_AT DESC;



/**********************************/
/* 5. 사진 번호를 이용한 상세 조회 */
/**********************************/

SELECT
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM MEMBER_PHOTO
WHERE member_photo_id = 1;



/**********************************/
/* 6. 회원 사진 등록 여부 확인 */
/**********************************/

SELECT COUNT(*) AS CNT
FROM MEMBER_PHOTO
WHERE MEMBER_ID = 1
  AND VISIBLE = 'Y';



/**********************************/
/* 7. 대표 사진 존재 여부 확인 */
/**********************************/

SELECT COUNT(*) AS CNT
FROM MEMBER_PHOTO
WHERE MEMBER_ID = 1
  AND IS_MAIN = 'Y'
  AND VISIBLE = 'Y';



/**********************************/
/* 8. 기존 대표 사진 해제 */
/**********************************/

UPDATE MEMBER_PHOTO
SET
    IS_MAIN = 'N',
    UPDATED_AT = SYSDATE
WHERE MEMBER_ID = 1
  AND IS_MAIN = 'Y';

COMMIT;



/**********************************/
/* 9. 새로운 대표 사진 등록 */
/**********************************/

INSERT INTO MEMBER_PHOTO (
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    MEMBER_PHOTO_SEQ.NEXTVAL,
    1,
    'FULL_BODY',
    'new_member1.jpg',
    'new_member1_20260813.jpg',
    '/member/photo/new_member1_20260813.jpg',
    488210,
    'Y',
    'Y',
    SYSDATE,
    SYSDATE
);

COMMIT;



/**********************************/
/* 10. 회원 사진 정보 수정 */
/**********************************/

UPDATE MEMBER_PHOTO
SET
    PHOTO_TYPE = 'FULL_BODY',
    FILE_ORIGIN = 'update_member1.jpg',
    FILE_SAVED = 'update_member1_saved.jpg',
    FILE_PATH = '/member/photo/update_member1_saved.jpg',
    FILE_SIZE = 530000,
    UPDATED_AT = SYSDATE
WHERE member_photo_id = 1;

COMMIT;



/**********************************/
/* 11. 대표 사진 변경 */
/**********************************/

-- 기존 대표 사진 해제
UPDATE MEMBER_PHOTO
SET
    IS_MAIN = 'N',
    UPDATED_AT = SYSDATE
WHERE MEMBER_ID = 1;


-- 지정한 사진을 대표 사진으로 변경
UPDATE MEMBER_PHOTO
SET
    IS_MAIN = 'Y',
    UPDATED_AT = SYSDATE
WHERE member_photo_id = 2
  AND MEMBER_ID = 1;

COMMIT;



/**********************************/
/* 12. 사진 사용 중지 */
/**********************************/

-- 실제 DELETE 대신 사용 여부만 N으로 변경 가능
UPDATE MEMBER_PHOTO
SET
    VISIBLE = 'N',
    IS_MAIN = 'N',
    UPDATED_AT = SYSDATE
WHERE member_photo_id = 1;

COMMIT;



/**********************************/
/* 13. 사진 다시 사용 */
/**********************************/

UPDATE MEMBER_PHOTO
SET
    VISIBLE = 'Y',
    UPDATED_AT = SYSDATE
WHERE member_photo_id = 1;

COMMIT;



/**********************************/
/* 14. 회원 사진 실제 삭제 */
/**********************************/

DELETE FROM MEMBER_PHOTO
WHERE member_photo_id = 1;

COMMIT;



/**********************************/
/* 15. 특정 회원 사진 전체 삭제 */
/**********************************/

DELETE FROM MEMBER_PHOTO
WHERE MEMBER_ID = 1;

COMMIT;



/**********************************/
/* 16. 전체 회원 사진 삭제 */
/**********************************/

DELETE FROM MEMBER_PHOTO;

COMMIT;



/**********************************/
/* 17. 회원 + 대표 사진 JOIN 조회 */
/**********************************/

SELECT
    M.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME,
    MP.member_photo_id,
    MP.PHOTO_TYPE,
    MP.FILE_ORIGIN,
    MP.FILE_SAVED,
    MP.FILE_PATH,
    MP.FILE_SIZE,
    MP.IS_MAIN,
    MP.VISIBLE
FROM MEMBER M
LEFT JOIN MEMBER_PHOTO MP
    ON M.MEMBER_ID = MP.MEMBER_ID
   AND MP.IS_MAIN = 'Y'
   AND MP.VISIBLE = 'Y'
ORDER BY M.MEMBER_ID ASC;



/**********************************/
/* 18. 사진이 없는 회원 조회 */
/**********************************/

SELECT
    M.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME
FROM MEMBER M
WHERE NOT EXISTS (
    SELECT 1
    FROM MEMBER_PHOTO MP
    WHERE MP.MEMBER_ID = M.MEMBER_ID
      AND MP.VISIBLE = 'Y'
)
ORDER BY M.MEMBER_ID ASC;



/**********************************/
/* 19. 대표 사진이 없는 회원 조회 */
/**********************************/

SELECT
    M.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME
FROM MEMBER M
WHERE NOT EXISTS (
    SELECT 1
    FROM MEMBER_PHOTO MP
    WHERE MP.MEMBER_ID = M.MEMBER_ID
      AND MP.IS_MAIN = 'Y'
      AND MP.VISIBLE = 'Y'
)
ORDER BY M.MEMBER_ID ASC;



/**********************************/
/* 20. 테이블 생성 여부 확인 */
/**********************************/

SELECT TABLE_NAME
FROM USER_TABLES
WHERE TABLE_NAME = 'MEMBER_PHOTO';


/**********************************/
/* 21. 시퀀스 생성 여부 확인 */
/**********************************/

SELECT SEQUENCE_NAME
FROM USER_SEQUENCES
WHERE SEQUENCE_NAME = 'MEMBER_PHOTO_SEQ';


/**********************************/
/* 22. 최종 전체 조회 */
/**********************************/

SELECT *
FROM MEMBER_PHOTO
ORDER BY member_photo_id ASC;

-----------------------------
SELECT *
FROM MEMBER_PHOTO
ORDER BY member_photo_id;

-----------------------------
DESC MEMBER_PHOTO;

SELECT
    constraint_name,
    constraint_type,
    status
FROM user_constraints
WHERE table_name = 'MEMBER_PHOTO';

SELECT
    constraint_name,
    column_name
FROM user_cons_columns
WHERE table_name = 'MEMBER_PHOTO'
ORDER BY constraint_name, position;

SELECT COUNT(*) AS CNT
FROM MEMBER_PHOTO;

SELECT *
FROM MEMBER_PHOTO;

DROP TABLE MEMBER_PHOTO CASCADE CONSTRAINTS;

DROP SEQUENCE MEMBER_PHOTO_SEQ;

CREATE TABLE MEMBER_PHOTO (
    member_photo_id NUMBER PRIMARY KEY,
    MEMBER_ID      NUMBER NOT NULL,
    PHOTO_TYPE     VARCHAR2(50) DEFAULT 'FACE' NOT NULL,
    FILE_ORIGIN    VARCHAR2(255) NOT NULL,
    FILE_SAVED     VARCHAR2(255) NOT NULL,
    FILE_PATH      VARCHAR2(500) NOT NULL,
    FILE_SIZE      NUMBER,
    IS_MAIN        VARCHAR2(1) DEFAULT 'Y' NOT NULL,
    VISIBLE        VARCHAR2(1) DEFAULT 'Y' NOT NULL,
    CREATED_AT     TIMESTAMP DEFAULT SYSDATE NOT NULL,
    UPDATED_AT     TIMESTAMP,

    CONSTRAINT FK_MEMBER_PHOTO_MEMBER
        FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER(MEMBER_ID),

    CONSTRAINT CK_MEMBER_PHOTO_MAIN
        CHECK (IS_MAIN IN ('Y', 'N')),

    CONSTRAINT CK_MEMBER_PHOTO_VISIBLE
        CHECK (VISIBLE IN ('Y', 'N'))
);

CREATE SEQUENCE MEMBER_PHOTO_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999
    CACHE 2
    NOCYCLE;

DESC MEMBER_PHOTO;

SELECT
    member_photo_id,
    MEMBER_ID,
    PHOTO_TYPE,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    IS_MAIN,
    VISIBLE,
    CREATED_AT
FROM MEMBER_PHOTO
WHERE MEMBER_ID = 7
ORDER BY member_photo_id DESC;
