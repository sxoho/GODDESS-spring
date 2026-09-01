-- ChatMessage 테이블 및 시퀀스 생성

CREATE SEQUENCE chat_message_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE chat_message (
    chat_message_id NUMBER PRIMARY KEY, -- 채팅 메시지 번호 (기본키)
    chat_room_id NUMBER NOT NULL, -- 채팅방 번호 (외래키, chat_room 테이블 참조)
    sender_type VARCHAR2(50) NOT NULL, -- 발신자 유형(USER/BOT/COUNSELOR)
    sender_member_id NUMBER, -- 발신자 회원 번호 (외래키, member 테이블 참조)
    message CLOB NOT NULL, -- 메시지 내용
    message_type VARCHAR2(50) NOT NULL, -- 메시지 유형(TEXT/IMAGE/PRODUCT_CARD/FITTING_CARD)
    is_read VARCHAR2(1) NOT NULL, -- 읽음 여부(Y/N)
    created_at TIMESTAMP DEFAULT SYSDATE, -- 등록일
    FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id),
    FOREIGN KEY (sender_member_id) REFERENCES member(member_id)
);

-- 코멘트
COMMENT ON TABLE chat_message IS '채팅 메시지';
COMMENT ON COLUMN chat_message.chat_message_id IS '채팅 메시지 번호';
COMMENT ON COLUMN chat_message.chat_room_id IS '채팅방 번호';
COMMENT ON COLUMN chat_message.sender_type IS '발신자 유형(USER/BOT/COUNSELOR)';
COMMENT ON COLUMN chat_message.sender_member_id IS '발신자 회원 번호';
COMMENT ON COLUMN chat_message.message IS '메시지 내용';
COMMENT ON COLUMN chat_message.message_type IS '메시지 유형(TEXT/IMAGE/PRODUCT_CARD/FITTING_CARD)';
COMMENT ON COLUMN chat_message.is_read IS '읽음 여부(Y/N)';
COMMENT ON COLUMN chat_message.created_at IS '등록일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO chat_message (chat_message_id, chat_room_id, sender_type, sender_member_id, message, message_type, is_read, created_at)VALUES (chat_message_seq.NEXTVAL, 1, '테스트_sender_type_1', 1, '테스트_message_1', '테스트_message_type_1', 'Y', SYSDATE);
INSERT INTO chat_message (chat_message_id, chat_room_id, sender_type, sender_member_id, message, message_type, is_read, created_at)VALUES (chat_message_seq.NEXTVAL, 2, '테스트_sender_type_2', 2, '테스트_message_2', '테스트_message_type_2', 'Y', SYSDATE);
INSERT INTO chat_message (chat_message_id, chat_room_id, sender_type, sender_member_id, message, message_type, is_read, created_at)VALUES (chat_message_seq.NEXTVAL, 3, '테스트_sender_type_3', 3, '테스트_message_3', '테스트_message_type_3', 'N', SYSDATE);
COMMIT;
