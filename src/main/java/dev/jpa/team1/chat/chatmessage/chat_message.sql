/******************************************************************
Table Name: 챗봇 메시지
******************************************************************/

DROP TABLE CHAT_MESSAGE CASCADE CONSTRAINTS;
DROP SEQUENCE CHAT_MESSAGE_SEQ;


/******************************************************************
1. Sequence
******************************************************************/

CREATE SEQUENCE CHAT_MESSAGE_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;


/******************************************************************
2. CHAT_MESSAGE 테이블 생성
******************************************************************/

CREATE TABLE CHAT_MESSAGE (
    CHAT_MESSAGE_ID NUMBER PRIMARY KEY,
    CHAT_ROOM_ID NUMBER NOT NULL,
    SENDER_TYPE VARCHAR2(20) NOT NULL,
    MESSAGE CLOB NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,

    CONSTRAINT FK_CHAT_MESSAGE_ROOM
        FOREIGN KEY (CHAT_ROOM_ID)
        REFERENCES CHAT_ROOM(CHAT_ROOM_ID),

    CONSTRAINT CK_CHAT_MESSAGE_SENDER
        CHECK (SENDER_TYPE IN ('USER', 'BOT'))
);


/******************************************************************
3. Comments
******************************************************************/

COMMENT ON TABLE CHAT_MESSAGE IS 'GODDESS AI 챗봇 메시지';
COMMENT ON COLUMN CHAT_MESSAGE.CHAT_MESSAGE_ID IS '챗봇 메시지 번호';
COMMENT ON COLUMN CHAT_MESSAGE.CHAT_ROOM_ID IS '채팅방 번호';
COMMENT ON COLUMN CHAT_MESSAGE.SENDER_TYPE IS '발신자 유형(USER/BOT)';
COMMENT ON COLUMN CHAT_MESSAGE.MESSAGE IS '메시지 내용';
COMMENT ON COLUMN CHAT_MESSAGE.CREATED_AT IS '메시지 생성일';

COMMIT;


/******************************************************************
4. READ - 확인
******************************************************************/

SELECT *
FROM CHAT_MESSAGE
ORDER BY CHAT_MESSAGE_ID ASC;

DESC CHAT_ROOM;