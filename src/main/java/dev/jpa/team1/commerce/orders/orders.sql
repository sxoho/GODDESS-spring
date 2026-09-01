-- Orders 테이블 및 시퀀스 생성

CREATE SEQUENCE orders_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE orders (
    order_id NUMBER PRIMARY KEY, -- 주문 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    order_name VARCHAR2(255) NOT NULL, -- 주문명(대표 상품명 등)
    product_total NUMBER NOT NULL, -- 총 상품 금액
    delivery_fee NUMBER NOT NULL, -- 배송비
    discount NUMBER NOT NULL, -- 할인 금액
    total NUMBER NOT NULL, -- 최종 결제 금액
    status VARCHAR2(50) NOT NULL, -- 주문 상태
    created_at TIMESTAMP DEFAULT SYSDATE, -- 주문일
    cancel_date TIMESTAMP, -- 취소일
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

-- 코멘트
COMMENT ON TABLE orders IS '주문';
COMMENT ON COLUMN orders.order_id IS '주문 번호';
COMMENT ON COLUMN orders.member_id IS '회원 번호';
COMMENT ON COLUMN orders.order_name IS '주문명(대표 상품명 등)';
COMMENT ON COLUMN orders.product_total IS '총 상품 금액';
COMMENT ON COLUMN orders.delivery_fee IS '배송비';
COMMENT ON COLUMN orders.discount IS '할인 금액';
COMMENT ON COLUMN orders.total IS '최종 결제 금액';
COMMENT ON COLUMN orders.status IS '주문 상태';
COMMENT ON COLUMN orders.created_at IS '주문일';
COMMENT ON COLUMN orders.cancel_date IS '취소일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO orders (order_id, member_id, order_name, product_total, delivery_fee, discount, total, status, created_at, cancel_date) VALUES (orders_seq.NEXTVAL, 1, '오버핏 셔츠 외 1건', 1, 1, 1, 1, '결제완료', SYSDATE, SYSDATE);
INSERT INTO orders (order_id, member_id, order_name, product_total, delivery_fee, discount, total, status, created_at, cancel_date) VALUES (orders_seq.NEXTVAL, 2, '와이드 슬랙스', 2, 2, 2, 2, '배송중', SYSDATE, SYSDATE);
INSERT INTO orders (order_id, member_id, order_name, product_total, delivery_fee, discount, total, status, created_at, cancel_date) VALUES (orders_seq.NEXTVAL, 3, '기본 면티 3종', 3, 3, 3, 3, '배송완료', SYSDATE, SYSDATE);
COMMIT;
