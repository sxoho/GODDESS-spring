/******************************************************************
Table Name: 챗봇 대화방
******************************************************************/

DROP TABLE CHAT_ROOM CASCADE CONSTRAINTS;
DROP SEQUENCE CHAT_ROOM_SEQ;


/******************************************************************
1. Sequence
******************************************************************/

CREATE SEQUENCE CHAT_ROOM_SEQ
START WITH 1
INCREMENT BY 1
MAXVALUE 9999999
CACHE 2
NOCYCLE;


/******************************************************************
2. CHAT_ROOM 테이블 생성
******************************************************************/

CREATE TABLE CHAT_ROOM (
    CHAT_ROOM_ID  NUMBER        NOT NULL,
    MEMBER_ID     NUMBER        NOT NULL,
    STATUS        VARCHAR2(20)  DEFAULT 'ACTIVE' NOT NULL,
    CREATED_AT    TIMESTAMP     DEFAULT SYSDATE,
    UPDATED_AT    TIMESTAMP,
    CLOSED_AT     TIMESTAMP,

    PRIMARY KEY (CHAT_ROOM_ID),

    CONSTRAINT FK_CHAT_ROOM_MEMBER
        FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER(MEMBER_ID),

    CONSTRAINT CK_CHAT_ROOM_STATUS
        CHECK (STATUS IN ('ACTIVE', 'CLOSED'))
);


/******************************************************************
3. Comments
******************************************************************/

COMMENT ON TABLE CHAT_ROOM IS 'GODDESS AI 챗봇 대화방';
COMMENT ON COLUMN CHAT_ROOM.CHAT_ROOM_ID IS '채팅방 번호';
COMMENT ON COLUMN CHAT_ROOM.MEMBER_ID IS '회원 번호';
COMMENT ON COLUMN CHAT_ROOM.STATUS IS '대화방 상태(ACTIVE/CLOSED)';
COMMENT ON COLUMN CHAT_ROOM.CREATED_AT IS '대화방 생성일';
COMMENT ON COLUMN CHAT_ROOM.UPDATED_AT IS '대화방 수정일';
COMMENT ON COLUMN CHAT_ROOM.CLOSED_AT IS '대화방 종료일';

COMMIT;


/******************************************************************
4. READ - 확인
******************************************************************/

SELECT *
FROM CHAT_ROOM
ORDER BY CHAT_ROOM_ID DESC;
