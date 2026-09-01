-- Payment 테이블 및 시퀀스 생성

CREATE SEQUENCE payment_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE payment (
    payment_id NUMBER PRIMARY KEY, -- 결제 번호 (기본키)
    order_id NUMBER NOT NULL UNIQUE, -- 주문 번호 (외래키, orders 테이블 참조)
    payment_method VARCHAR2(50) NOT NULL, -- 결제 수단
    amount NUMBER NOT NULL, -- 결제 금액
    status VARCHAR2(50) NOT NULL, -- 결제 상태
    approval_no VARCHAR2(100), -- 결제 승인번호
    created_at TIMESTAMP DEFAULT SYSDATE, -- 결제일
    cancel_date TIMESTAMP, -- 취소일
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- 코멘트
COMMENT ON TABLE payment IS '결제 정보';
COMMENT ON COLUMN payment.payment_id IS '결제 번호';
COMMENT ON COLUMN payment.order_id IS '주문 번호';
COMMENT ON COLUMN payment.payment_method IS '결제 수단';
COMMENT ON COLUMN payment.amount IS '결제 금액';
COMMENT ON COLUMN payment.status IS '결제 상태';
COMMENT ON COLUMN payment.approval_no IS '결제 승인번호';
COMMENT ON COLUMN payment.created_at IS '결제일';
COMMENT ON COLUMN payment.cancel_date IS '취소일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO payment (payment_id, order_id, payment_method, amount, status, approval_no, created_at, cancel_date) VALUES (payment_seq.NEXTVAL, 1, '신용카드', 50000, 'SUCCESS', '테스트_approval_no_1', SYSDATE, SYSDATE);
INSERT INTO payment (payment_id, order_id, payment_method, amount, status, approval_no, created_at, cancel_date) VALUES (payment_seq.NEXTVAL, 2, '카카오페이', 39900, 'SUCCESS', '테스트_approval_no_2', SYSDATE, SYSDATE);
INSERT INTO payment (payment_id, order_id, payment_method, amount, status, approval_no, created_at, cancel_date) VALUES (payment_seq.NEXTVAL, 3, '무통장입금', 28000, 'WAITING', '테스트_approval_no_3', SYSDATE, SYSDATE);
COMMIT;
