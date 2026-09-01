CREATE SEQUENCE refresh_token_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE refresh_token
(
    token_id   NUMBER PRIMARY KEY,
    login_id   VARCHAR2(50)              NOT NULL UNIQUE, -- 회원 아이디 (1명당 1개의 Refresh Token 유지)
    token      VARCHAR2(500)             NOT NULL,        -- Refresh Token 문자열
    created_at TIMESTAMP DEFAULT SYSDATE NOT NULL,
    updated_at TIMESTAMP DEFAULT SYSDATE NOT NULL
);

-- 예시 데이터 등록 (Mock Data)
INSERT INTO refresh_token (token_id, login_id, token, created_at, updated_at) VALUES (refresh_token_seq.NEXTVAL, '테스트_login_id_1', '테스트_token_1', SYSDATE, SYSDATE);
INSERT INTO refresh_token (token_id, login_id, token, created_at, updated_at) VALUES (refresh_token_seq.NEXTVAL, '테스트_login_id_2', '테스트_token_2', SYSDATE, SYSDATE);
INSERT INTO refresh_token (token_id, login_id, token, created_at, updated_at) VALUES (refresh_token_seq.NEXTVAL, '테스트_login_id_3', '테스트_token_3', SYSDATE, SYSDATE);
COMMIT;
