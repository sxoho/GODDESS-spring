/**************************************************
1. LOGIN_HISTORY 시퀀스 생성
**************************************************/
/왜 깃에 안뜨냐고 

CREATE SEQUENCE LOGIN_HISTORY_SEQ
START WITH 1
INCREMENT BY 1
MAXVALUE 9999999
CACHE 2
NOCYCLE;


/**************************************************
2. LOGIN_HISTORY 테이블 생성
**************************************************/

CREATE TABLE LOGIN_HISTORY (
    login_history_id NUMBER NOT NULL,
    MEMBER_ID       NUMBER NOT NULL,
    SUCCESS         VARCHAR2(1) NOT NULL,
    IP              VARCHAR2(50),
    DEVICE          VARCHAR2(255),
    CREATED_AT      TIMESTAMP DEFAULT SYSDATE,
    LOGOUT_DATE     TIMESTAMP,

    PRIMARY KEY (login_history_id),

    CONSTRAINT FK_LOGIN_HISTORY_MEMBER
        FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER(MEMBER_ID),

    CONSTRAINT CK_LOGIN_HISTORY_SUCCESS
        CHECK (SUCCESS IN ('Y', 'N'))
);


/**************************************************
3. 테이블 및 컬럼 설명
**************************************************/

COMMENT ON TABLE LOGIN_HISTORY
IS '로그인 내역';

COMMENT ON COLUMN LOGIN_HISTORY.login_history_id
IS '로그인 내역 번호';

COMMENT ON COLUMN LOGIN_HISTORY.MEMBER_ID
IS '회원 번호';

COMMENT ON COLUMN LOGIN_HISTORY.SUCCESS
IS '로그인 성공 여부 (Y/N)';

COMMENT ON COLUMN LOGIN_HISTORY.IP
IS '접속 IP';

COMMENT ON COLUMN LOGIN_HISTORY.DEVICE
IS '접속 기기/브라우저';

COMMENT ON COLUMN LOGIN_HISTORY.CREATED_AT
IS '로그인 일시';

COMMENT ON COLUMN LOGIN_HISTORY.LOGOUT_DATE
IS '로그아웃 일시';


/**************************************************
4. LOGIN_HISTORY 예시 데이터 등록
**************************************************/

-- 일반 회원 로그인 성공
INSERT INTO LOGIN_HISTORY (
    login_history_id,
    MEMBER_ID,
    SUCCESS,
    IP,
    DEVICE,
    CREATED_AT,
    LOGOUT_DATE
)
VALUES (
    LOGIN_HISTORY_SEQ.NEXTVAL,
    1,
    'Y',
    '127.0.0.1',
    'Chrome / Windows',
    SYSDATE,
    NULL
);


-- 다른 회원 로그인 성공
INSERT INTO LOGIN_HISTORY (
    login_history_id,
    MEMBER_ID,
    SUCCESS,
    IP,
    DEVICE,
    CREATED_AT,
    LOGOUT_DATE
)
VALUES (
    LOGIN_HISTORY_SEQ.NEXTVAL,
    2,
    'Y',
    '192.168.0.10',
    'Edge / Windows',
    SYSDATE,
    NULL
);


-- 로그인 실패
INSERT INTO LOGIN_HISTORY (
    login_history_id,
    MEMBER_ID,
    SUCCESS,
    IP,
    DEVICE,
    CREATED_AT,
    LOGOUT_DATE
)
VALUES (
    LOGIN_HISTORY_SEQ.NEXTVAL,
    3,
    'N',
    '192.168.0.20',
    'Chrome / Windows',
    SYSDATE,
    NULL
);

COMMIT;


/**************************************************
5. 전체 로그인 내역 조회
**************************************************/

SELECT
    login_history_id,
    MEMBER_ID,
    SUCCESS,
    IP,
    DEVICE,
    CREATED_AT,
    LOGOUT_DATE
FROM LOGIN_HISTORY
ORDER BY login_history_id ASC;


/**************************************************
6. 회원 정보 포함 로그인 내역 조회
**************************************************/

SELECT
    LH.login_history_id,
    LH.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME,
    LH.SUCCESS,
    LH.IP,
    LH.DEVICE,
    LH.CREATED_AT,
    LH.LOGOUT_DATE
FROM LOGIN_HISTORY LH
JOIN MEMBER M
    ON LH.MEMBER_ID = M.MEMBER_ID
ORDER BY LH.login_history_id DESC;


/**************************************************
7. 특정 회원 로그인 내역 조회
**************************************************/

SELECT
    login_history_id,
    MEMBER_ID,
    SUCCESS,
    IP,
    DEVICE,
    CREATED_AT,
    LOGOUT_DATE
FROM LOGIN_HISTORY
WHERE MEMBER_ID = 1
ORDER BY CREATED_AT DESC;


/**************************************************
8. 로그인 성공 내역 조회
**************************************************/

SELECT
    login_history_id,
    MEMBER_ID,
    SUCCESS,
    IP,
    DEVICE,
    CREATED_AT,
    LOGOUT_DATE
FROM LOGIN_HISTORY
WHERE SUCCESS = 'Y'
ORDER BY CREATED_AT DESC;


/**************************************************
9. 로그인 실패 내역 조회
**************************************************/

SELECT
    login_history_id,
    MEMBER_ID,
    SUCCESS,
    IP,
    DEVICE,
    CREATED_AT,
    LOGOUT_DATE
FROM LOGIN_HISTORY
WHERE SUCCESS = 'N'
ORDER BY CREATED_AT DESC;


/**************************************************
10. 전체 로그인 내역 수
**************************************************/

SELECT COUNT(*) AS LOGIN_HISTORY_COUNT
FROM LOGIN_HISTORY;


/**************************************************
11. 로그인 성공 횟수
**************************************************/

SELECT COUNT(*) AS LOGIN_SUCCESS_COUNT
FROM LOGIN_HISTORY
WHERE SUCCESS = 'Y';


/**************************************************
12. 로그인 실패 횟수
**************************************************/

SELECT COUNT(*) AS LOGIN_FAIL_COUNT
FROM LOGIN_HISTORY
WHERE SUCCESS = 'N';


/**************************************************
13. 회원별 로그인 횟수
**************************************************/

SELECT
    MEMBER_ID,
    COUNT(*) AS LOGIN_COUNT
FROM LOGIN_HISTORY
GROUP BY MEMBER_ID
ORDER BY MEMBER_ID ASC;


/**************************************************
14. 회원별 로그인 성공/실패 횟수
**************************************************/

SELECT
    MEMBER_ID,
    SUCCESS,
    COUNT(*) AS LOGIN_COUNT
FROM LOGIN_HISTORY
GROUP BY MEMBER_ID, SUCCESS
ORDER BY MEMBER_ID ASC, SUCCESS ASC;


/**************************************************
15. 로그아웃 시간 등록
**************************************************/

UPDATE LOGIN_HISTORY
SET LOGOUT_DATE = SYSDATE
WHERE login_history_id = 1;

COMMIT;


/**************************************************
16. 특정 로그인 내역 수정 테스트
**************************************************/

UPDATE LOGIN_HISTORY
SET
    IP = '127.0.0.2',
    DEVICE = 'Chrome / Windows 수정'
WHERE login_history_id = 1;

COMMIT;


/**************************************************
17. 특정 로그인 내역 삭제 테스트
**************************************************/

DELETE FROM LOGIN_HISTORY
WHERE login_history_id = 3;

COMMIT;


/**************************************************
18. 최종 로그인 내역 확인
**************************************************/

SELECT
    LH.login_history_id,
    LH.MEMBER_ID,
    M.LOGIN_ID,
    M.NAME,
    M.NICKNAME,
    LH.SUCCESS,
    LH.IP,
    LH.DEVICE,
    LH.CREATED_AT,
    LH.LOGOUT_DATE
FROM LOGIN_HISTORY LH
JOIN MEMBER M
    ON LH.MEMBER_ID = M.MEMBER_ID
ORDER BY LH.login_history_id ASC;


/**************************************************
19. 전체 데이터 삭제 테스트
실제 데이터 사용 중에는 실행하지 않음
**************************************************/

-- DELETE FROM LOGIN_HISTORY;
-- COMMIT;


/**************************************************
20. 테이블 / 시퀀스 삭제
재생성이 필요할 때만 실행
**************************************************/

-- DROP TABLE LOGIN_HISTORY CASCADE CONSTRAINTS;
-- DROP SEQUENCE LOGIN_HISTORY_SEQ;