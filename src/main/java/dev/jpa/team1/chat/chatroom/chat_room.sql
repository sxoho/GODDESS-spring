-- ChatRoom 테이블 및 시퀀스 생성

CREATE SEQUENCE chat_room_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE chat_room (
    chat_room_id NUMBER PRIMARY KEY, -- 채팅방 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    counselor_member_id NUMBER, -- 담당 상담원 번호 (외래키, member 테이블 참조)
    product_id NUMBER, -- 연관 상품 번호 (외래키, product 테이블 참조)
    order_id NUMBER, -- 연관 주문 번호 (외래키, orders 테이블 참조)
    chat_type VARCHAR2(50) NOT NULL, -- 상담 유형(CHATBOT/LIVE_CHAT)
    status VARCHAR2(50) NOT NULL, -- 상담 상태(ACTIVE/WAITING/CLOSED)
    created_at TIMESTAMP DEFAULT SYSDATE, -- 최초 생성일
    closed_date TIMESTAMP, -- 종료일
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (counselor_member_id) REFERENCES member(member_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- 코멘트
COMMENT ON TABLE chat_room IS '채팅방';
COMMENT ON COLUMN chat_room.chat_room_id IS '채팅방 번호';
COMMENT ON COLUMN chat_room.member_id IS '회원 번호';
COMMENT ON COLUMN chat_room.counselor_member_id IS '담당 상담원 번호';
COMMENT ON COLUMN chat_room.product_id IS '연관 상품 번호';
COMMENT ON COLUMN chat_room.order_id IS '연관 주문 번호';
COMMENT ON COLUMN chat_room.chat_type IS '상담 유형(CHATBOT/LIVE_CHAT)';
COMMENT ON COLUMN chat_room.status IS '상담 상태(ACTIVE/WAITING/CLOSED)';
COMMENT ON COLUMN chat_room.created_at IS '최초 생성일';
COMMENT ON COLUMN chat_room.closed_date IS '종료일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO chat_room (chat_room_id, member_id, counselor_member_id, product_id, order_id, chat_type, status, created_at, closed_date) VALUES (chat_room_seq.NEXTVAL, 1, 1, 1, 1, '테스트_chat_type_1', '활성', SYSDATE, SYSDATE);
INSERT INTO chat_room (chat_room_id, member_id, counselor_member_id, product_id, order_id, chat_type, status, created_at, closed_date) VALUES (chat_room_seq.NEXTVAL, 2, 2, 2, 2, '테스트_chat_type_2', '종료', SYSDATE, SYSDATE);
INSERT INTO chat_room (chat_room_id, member_id, counselor_member_id, product_id, order_id, chat_type, status, created_at, closed_date) VALUES (chat_room_seq.NEXTVAL, 3, 3, 3, 3, '테스트_chat_type_3', '활성', SYSDATE, SYSDATE);
COMMIT;
