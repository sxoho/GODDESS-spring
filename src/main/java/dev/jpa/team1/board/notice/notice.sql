-- SQL을 직접 적용하는 경우 테스트
/**********************************/
/* Table Name: 공지사항 */
/**********************************/


/* =========================================================
   0. 기존 테이블 / 시퀀스 삭제
   ========================================================= */

DROP TABLE NOTICE CASCADE CONSTRAINTS;

DROP SEQUENCE NOTICE_SEQ;


/* =========================================================
   1. NOTICE 테이블 생성
   ========================================================= */

CREATE TABLE NOTICE (
    NOTICE_ID    NUMBER          NOT NULL,
    MEMBER_ID    NUMBER          NOT NULL,
    WRITER_NAME  VARCHAR2(100)   DEFAULT '관리자' NOT NULL,
    TITLE        VARCHAR2(255)   NOT NULL,
    CONTENT      CLOB            NOT NULL,
    LINK_URL     VARCHAR2(1000),
    FILE_ORIGIN  VARCHAR2(255),
    FILE_SAVED   VARCHAR2(255),
    FILE_PATH    VARCHAR2(500),
    FILE_SIZE    NUMBER,
    VIEW_COUNT   NUMBER          DEFAULT 0 NOT NULL,
    FIXED        VARCHAR2(1)     DEFAULT 'N' NOT NULL,
    VISIBLE      VARCHAR2(1)     DEFAULT 'Y' NOT NULL,
    CREATED_AT   TIMESTAMP       DEFAULT SYSDATE NOT NULL,
    UPDATED_AT   TIMESTAMP,

    PRIMARY KEY (NOTICE_ID),

    CONSTRAINT FK_NOTICE_MEMBER
        FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER(MEMBER_ID),

    CONSTRAINT CK_NOTICE_FIXED
        CHECK (FIXED IN ('Y', 'N')),

    CONSTRAINT CK_NOTICE_VISIBLE
        CHECK (VISIBLE IN ('Y', 'N'))
);


/* =========================================================
   2. COMMENT
   ========================================================= */

COMMENT ON TABLE NOTICE IS '공지사항';

COMMENT ON COLUMN NOTICE.NOTICE_ID IS '공지 번호';

COMMENT ON COLUMN NOTICE.MEMBER_ID IS '공지 작성 관리자 회원 번호';

COMMENT ON COLUMN NOTICE.TITLE IS '제목';

COMMENT ON COLUMN NOTICE.CONTENT IS '내용';

COMMENT ON COLUMN NOTICE.VIEW_COUNT IS '조회수';

COMMENT ON COLUMN NOTICE.FIXED IS '상단 고정 여부(Y/N)';

COMMENT ON COLUMN NOTICE.VISIBLE IS '공개 여부(Y/N)';

COMMENT ON COLUMN NOTICE.CREATED_AT IS '등록일';

COMMENT ON COLUMN NOTICE.UPDATED_AT IS '수정일';

COMMENT ON COLUMN NOTICE.WRITER_NAME IS '화면 표시용 작성자 이름';

COMMENT ON COLUMN NOTICE.LINK_URL IS '관련 링크';

COMMENT ON COLUMN NOTICE.FILE_ORIGIN IS '첨부파일 원본 파일명';

COMMENT ON COLUMN NOTICE.FILE_SAVED IS '서버 저장 파일명';

COMMENT ON COLUMN NOTICE.FILE_PATH IS '첨부파일 저장 경로';

COMMENT ON COLUMN NOTICE.FILE_SIZE IS '첨부파일 크기';


/* =========================================================
   3. NOTICE 시퀀스 생성
   ========================================================= */

CREATE SEQUENCE NOTICE_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 9999999
    CACHE 2
    NOCYCLE;


COMMIT;


/* =========================================================
   4. 관리자 회원 확인
   ========================================================= */

-- 공지사항 등록 전에
-- MEMBER_ID = 4인 관리자 계정이 존재하는지 확인

SELECT
    MEMBER_ID,
    LOGIN_ID,
    NAME,
    NICKNAME,
    GRADE_ID,
    STATUS
FROM MEMBER
WHERE MEMBER_ID = 4;


/* =========================================================
   5. 공지사항 전체 삭제 테스트
   ========================================================= */

-- 주의:
-- 테스트 데이터를 모두 지울 때만 사용

DELETE FROM NOTICE;

COMMIT;


/* =========================================================
   6. 공지사항 등록 INSERT
   ========================================================= */

-- 1번 공지
-- 상단 고정 + 공개

INSERT INTO NOTICE (
    NOTICE_ID,
    MEMBER_ID,
    WRITER_NAME,
    TITLE,
    CONTENT,
    LINK_URL,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    NOTICE_SEQ.NEXTVAL,
    4,
    '관리자',
    '추석 연휴 배송 안내',
    '추석 연휴 기간 동안 배송 일정이 일부 지연될 수 있습니다.',
    'https://example.com',
    NULL,
    NULL,
    NULL,
    NULL,
    0,
    'Y',
    'Y',
    SYSDATE,
    NULL
);


-- 2번 공지
-- 일반 공지 + 공개

INSERT INTO NOTICE (
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    CONTENT,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    NOTICE_SEQ.NEXTVAL,
    4,
    '시스템 점검 안내',
    '더 나은 서비스를 위해 새벽 시간 동안 시스템 점검이 진행될 예정입니다.',
    0,
    'N',
    'Y',
    SYSDATE,
    NULL
);


-- 3번 공지
-- 일반 공지 + 비공개

INSERT INTO NOTICE (
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    CONTENT,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
)
VALUES (
    NOTICE_SEQ.NEXTVAL,
    4,
    '신규 기능 업데이트 예정 안내',
    'AI 피팅 관련 신규 기능 업데이트를 준비하고 있습니다.',
    0,
    'N',
    'N',
    SYSDATE,
    NULL
);


COMMIT;

/* =====================================================*/
SELECT *
FROM NOTICE
ORDER BY NOTICE_ID ASC;


/* =========================================================
   7. 전체 공지사항 조회
   ========================================================= */

SELECT
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM NOTICE
ORDER BY NOTICE_ID ASC;


/* =========================================================
   8. 관리자 공지사항 목록 조회
   ========================================================= */

-- 관리자 화면
-- 공개 / 비공개 전부 조회
-- 상단 고정 공지 먼저
-- 그 다음 최신 등록순

SELECT
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM NOTICE
ORDER BY
    FIXED DESC,
    CREATED_AT DESC;


/* =========================================================
   9. 일반 사용자용 공개 공지사항 목록 조회
   ========================================================= */

-- NOTICE 일반 화면
-- VISIBLE = 'Y'만 조회

SELECT
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT
FROM NOTICE
WHERE VISIBLE = 'Y'
ORDER BY
    FIXED DESC,
    CREATED_AT DESC;


/* =========================================================
   10. 공지사항 상세 조회
   ========================================================= */

-- NOTICE_ID = 1 공지 조회

SELECT
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    CONTENT,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM NOTICE
WHERE NOTICE_ID = 1;


/* =========================================================
   11. 조회수 증가
   ========================================================= */

UPDATE NOTICE
SET VIEW_COUNT = VIEW_COUNT + 1
WHERE NOTICE_ID = 1;

COMMIT;


-- 조회수 증가 확인

SELECT
    NOTICE_ID,
    TITLE,
    VIEW_COUNT
FROM NOTICE
WHERE NOTICE_ID = 1;


/* =========================================================
   12. 공지사항 수정
   ========================================================= */

UPDATE NOTICE
SET
    TITLE = '추석 연휴 배송 안내 수정',
    CONTENT = '추석 연휴 배송 일정을 수정하여 안내드립니다.',
    FIXED = 'Y',
    VISIBLE = 'Y',
    UPDATED_AT = SYSDATE
WHERE NOTICE_ID = 1;

COMMIT;


/* =========================================================
   13. 공지사항 수정 확인
   ========================================================= */

SELECT
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    CONTENT,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM NOTICE
WHERE NOTICE_ID = 1;


/* =========================================================
   14. 상단 고정 여부 변경
   ========================================================= */

-- 상단 고정 해제

UPDATE NOTICE
SET
    FIXED = 'N',
    UPDATED_AT = SYSDATE
WHERE NOTICE_ID = 1;

COMMIT;


/* =========================================================
   15. 공개 여부 변경
   ========================================================= */

-- 비공개 처리

UPDATE NOTICE
SET
    VISIBLE = 'N',
    UPDATED_AT = SYSDATE
WHERE NOTICE_ID = 2;

COMMIT;


/* =========================================================
   16. 제목 검색
   ========================================================= */

SELECT
    NOTICE_ID,
    TITLE,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT
FROM NOTICE
WHERE TITLE LIKE '%배송%'
ORDER BY CREATED_AT DESC;


/* =========================================================
   17. 특정 관리자가 작성한 공지 조회
   ========================================================= */

SELECT
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT
FROM NOTICE
WHERE MEMBER_ID = 4
ORDER BY CREATED_AT DESC;


/* =========================================================
   18. 공지사항 삭제
   ========================================================= */

-- NOTICE_ID = 3 삭제 테스트

DELETE FROM NOTICE
WHERE NOTICE_ID = 3;

COMMIT;


/* =========================================================
   19. 삭제 확인
   ========================================================= */

SELECT
    NOTICE_ID,
    MEMBER_ID,
    TITLE,
    VIEW_COUNT,
    FIXED,
    VISIBLE,
    CREATED_AT,
    UPDATED_AT
FROM NOTICE
ORDER BY NOTICE_ID ASC;


/* =========================================================
   20. NOTICE 테이블 존재 여부 확인
   ========================================================= */

SELECT TABLE_NAME
FROM USER_TABLES
WHERE TABLE_NAME = 'NOTICE';


/* =========================================================
   21. NOTICE 시퀀스 확인
   ========================================================= */

SELECT
    SEQUENCE_NAME,
    MIN_VALUE,
    MAX_VALUE,
    INCREMENT_BY,
    LAST_NUMBER
FROM USER_SEQUENCES
WHERE SEQUENCE_NAME = 'NOTICE_SEQ';


/* =========================================================
   22. 최종 전체 조회
   ========================================================= */

SELECT *
FROM NOTICE
ORDER BY
    FIXED DESC,
    CREATED_AT DESC;
    
    
-----------------------------------

ALTER TABLE NOTICE ADD (
    WRITER_NAME VARCHAR2(100) DEFAULT '관리자' NOT NULL,
    LINK_URL    VARCHAR2(1000),
    FILE_ORIGIN VARCHAR2(255),
    FILE_SAVED  VARCHAR2(255),
    FILE_PATH   VARCHAR2(500),
    FILE_SIZE   NUMBER
);

COMMENT ON COLUMN NOTICE.WRITER_NAME IS '화면 표시용 작성자 이름';
COMMENT ON COLUMN NOTICE.LINK_URL IS '관련 링크';
COMMENT ON COLUMN NOTICE.FILE_ORIGIN IS '첨부파일 원본 파일명';
COMMENT ON COLUMN NOTICE.FILE_SAVED IS '서버 저장 파일명';
COMMENT ON COLUMN NOTICE.FILE_PATH IS '첨부파일 저장 경로';
COMMENT ON COLUMN NOTICE.FILE_SIZE IS '첨부파일 크기';

COMMIT;

SELECT
    NOTICE_ID,
    MEMBER_ID,
    WRITER_NAME,
    TITLE,
    LINK_URL,
    FILE_ORIGIN,
    FILE_SAVED,
    FILE_PATH,
    FILE_SIZE
FROM NOTICE
ORDER BY NOTICE_ID;

SELECT
    NOTICE_ID,
    TITLE,
    WRITER_NAME,
    VISIBLE,
    FILE_ORIGIN,
    FILE_SAVED,
    LINK_URL,
    CREATED_AT
FROM NOTICE
WHERE TITLE LIKE '%공지사항%'
ORDER BY NOTICE_ID DESC;