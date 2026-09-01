/******************************************************************

Table Name: 피팅 프로필

******************************************************************/


/******************************************************************

1. 테이블 / Sequence 생성

※ 새로운 DB에서 최초 구성할 때만 실행

※ 현재 프로젝트 DB에는 FITTING_PROFILE 테이블과

   FITTING_PROFILE_SEQ가 이미 존재하므로

   임의로 전체 실행하지 않는다.

******************************************************************/

/*

CREATE SEQUENCE FITTING_PROFILE_SEQ

START WITH 1

INCREMENT BY 1

MAXVALUE 9999999

CACHE 2

NOCYCLE;


CREATE TABLE FITTING_PROFILE (

    FITTINGPROFILE_ID  NUMBER       NOT NULL,

    MEMBER_ID          NUMBER       NOT NULL,

    HEIGHT             NUMBER(5,1),

    BODY_TYPE          VARCHAR2(50),

    TOP_SIZE           VARCHAR2(20),

    BOTTOM_SIZE        VARCHAR2(20),

    PREFERRED_FIT      VARCHAR2(50),

    CREATED_AT         DATE DEFAULT SYSDATE NOT NULL,

    UPDATED_AT         DATE,

    CONSTRAINT PK_FITTING_PROFILE

        PRIMARY KEY (

            FITTINGPROFILE_ID

        ),

    CONSTRAINT FK_FITTING_PROFILE_MEMBER

        FOREIGN KEY (

            MEMBER_ID

        )

        REFERENCES MEMBER (

            MEMBER_ID

        )

);


COMMENT ON TABLE FITTING_PROFILE

IS '피팅 프로필';

COMMENT ON COLUMN FITTING_PROFILE.FITTINGPROFILE_ID

IS '피팅 프로필 번호';

COMMENT ON COLUMN FITTING_PROFILE.MEMBER_ID

IS '회원 번호';

COMMENT ON COLUMN FITTING_PROFILE.HEIGHT

IS '키';

COMMENT ON COLUMN FITTING_PROFILE.BODY_TYPE

IS '체형 선택값';

COMMENT ON COLUMN FITTING_PROFILE.TOP_SIZE

IS '평소 상의 사이즈';

COMMENT ON COLUMN FITTING_PROFILE.BOTTOM_SIZE

IS '평소 하의 사이즈';

COMMENT ON COLUMN FITTING_PROFILE.PREFERRED_FIT

IS '선호 핏';

COMMENT ON COLUMN FITTING_PROFILE.CREATED_AT

IS '등록일';

COMMENT ON COLUMN FITTING_PROFILE.UPDATED_AT

IS '수정일';

*/


/******************************************************************

2. 현재 FITTING_PROFILE 컬럼 확인

******************************************************************/

SELECT

    COLUMN_NAME,

    DATA_TYPE,

    DATA_LENGTH,

    DATA_PRECISION,

    DATA_SCALE,

    NULLABLE

FROM USER_TAB_COLUMNS

WHERE TABLE_NAME = 'FITTING_PROFILE'

ORDER BY COLUMN_ID;


/******************************************************************

3. Sequence 존재 확인

******************************************************************/

SELECT

    SEQUENCE_NAME

FROM USER_SEQUENCES

WHERE SEQUENCE_NAME = 'FITTING_PROFILE_SEQ';


/******************************************************************

4. 피팅 프로필 전체 조회

******************************************************************/

SELECT

    FITTINGPROFILE_ID,

    MEMBER_ID,

    HEIGHT,

    BODY_TYPE,

    TOP_SIZE,

    BOTTOM_SIZE,

    PREFERRED_FIT,

    CREATED_AT,

    UPDATED_AT

FROM FITTING_PROFILE

ORDER BY FITTINGPROFILE_ID ASC;


/******************************************************************

5. 특정 회원 피팅 프로필 조회

******************************************************************/

SELECT

    FITTINGPROFILE_ID,

    MEMBER_ID,

    HEIGHT,

    BODY_TYPE,

    TOP_SIZE,

    BOTTOM_SIZE,

    PREFERRED_FIT,

    CREATED_AT,

    UPDATED_AT

FROM FITTING_PROFILE

WHERE MEMBER_ID = 4;


/******************************************************************

6. 회원 정보 + 피팅 프로필 JOIN 조회

******************************************************************/

SELECT

    FP.FITTINGPROFILE_ID,

    M.MEMBER_ID,

    M.LOGIN_ID,

    M.NAME,

    M.NICKNAME,

    FP.HEIGHT,

    FP.BODY_TYPE,

    FP.TOP_SIZE,

    FP.BOTTOM_SIZE,

    FP.PREFERRED_FIT,

    FP.CREATED_AT,

    FP.UPDATED_AT

FROM FITTING_PROFILE FP

JOIN MEMBER M

    ON FP.MEMBER_ID = M.MEMBER_ID

ORDER BY FP.FITTINGPROFILE_ID ASC;


/******************************************************************

7. 특정 회원 피팅 프로필 존재 여부 확인

******************************************************************/

SELECT

    COUNT(*) AS CNT

FROM FITTING_PROFILE

WHERE MEMBER_ID = 4;


/*******************************************************************

8. CREATE 테스트

※ 실제 존재하는 MEMBER_ID를 사용한다.

※ 필요할 때만 아래 INSERT 부분을 개별 실행한다.

※ Spring API 등록 테스트가 완료되어 있으므로

   평소에는 실행하지 않는다.

******************************************************************/

/*

INSERT INTO FITTING_PROFILE (

    FITTINGPROFILE_ID,

    MEMBER_ID,

    HEIGHT,

    BODY_TYPE,

    TOP_SIZE,

    BOTTOM_SIZE,

    PREFERRED_FIT,

    CREATED_AT,

    UPDATED_AT

)

VALUES (

    FITTING_PROFILE_SEQ.NEXTVAL,

    4,

    165.5,

    'NORMAL',

    'M',

    'M',

    'REGULAR',

    SYSDATE,

    NULL

);

COMMIT;

*/


/******************************************************************

9. UPDATE 테스트

※ 필요할 때만 개별 실행

******************************************************************/

/*

UPDATE FITTING_PROFILE

SET

    HEIGHT = 166.0,

    BODY_TYPE = 'SLIM',

    TOP_SIZE = 'S',

    BOTTOM_SIZE = 'M',

    PREFERRED_FIT = 'RELAXED',

    UPDATED_AT = SYSDATE

WHERE MEMBER_ID = 4;

COMMIT;

*/


/******************************************************************

10. DELETE 테스트

※ 실제 데이터 삭제이므로 기본적으로 실행하지 않는다.

******************************************************************/

/*

DELETE FROM FITTING_PROFILE

WHERE MEMBER_ID = 4;

COMMIT;

*/


---------------------

SELECT COUNT(*) AS CNT
FROM FITTING_PROFILE;

SELECT COUNT(*) AS NULL_CREATED_AT_CNT
FROM FITTING_PROFILE
WHERE CREATED_AT IS NULL;

SELECT *
FROM FITTING_PROFILE;

ALTER TABLE FITTING_PROFILE
RENAME COLUMN FITTING_PROFILE_ID TO FITTINGPROFILE_ID;


SELECT COLUMN_NAME
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'FITTING_PROFILE'
ORDER BY COLUMN_ID;

ALTER TABLE FITTING_PROFILE
ADD CREATED_AT_TEMP DATE;

UPDATE FITTING_PROFILE
SET CREATED_AT_TEMP = CAST(CREATED_AT AS DATE);

COMMIT;

SELECT CREATED_AT, CREATED_AT_TEMP
FROM FITTING_PROFILE;

ALTER TABLE FITTING_PROFILE
DROP COLUMN CREATED_AT;

ALTER TABLE FITTING_PROFILE
RENAME COLUMN CREATED_AT_TEMP TO CREATED_AT;

ALTER TABLE FITTING_PROFILE
MODIFY CREATED_AT DEFAULT SYSDATE NOT NULL;

ALTER TABLE FITTING_PROFILE
ADD UPDATED_AT_TEMP DATE;

UPDATE FITTING_PROFILE
SET UPDATED_AT_TEMP = CAST(UPDATED_AT AS DATE);

COMMIT;

SELECT UPDATED_AT, UPDATED_AT_TEMP
FROM FITTING_PROFILE;

ALTER TABLE FITTING_PROFILE
DROP COLUMN UPDATED_AT;

ALTER TABLE FITTING_PROFILE
RENAME COLUMN UPDATED_AT_TEMP TO UPDATED_AT;

SELECT
    COLUMN_NAME,
    DATA_TYPE,
    DATA_LENGTH,
    DATA_PRECISION,
    DATA_SCALE,
    NULLABLE,
    DATA_DEFAULT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'FITTING_PROFILE'
ORDER BY COLUMN_ID;