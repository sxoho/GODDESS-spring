-- Delivery 테이블 및 시퀀스 생성

CREATE SEQUENCE delivery_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE delivery (
    delivery_id NUMBER PRIMARY KEY, -- 배송 번호 (기본키)
    order_id NUMBER NOT NULL UNIQUE, -- 주문 번호 (외래키, orders 테이블 참조)
    recipient VARCHAR2(50) NOT NULL, -- 수령인
    tel VARCHAR2(20) NOT NULL, -- 연락처
    zipcode VARCHAR2(10) NOT NULL, -- 우편번호
    address1 VARCHAR2(255) NOT NULL, -- 기본 주소
    address2 VARCHAR2(255) NOT NULL, -- 상세 주소
    request VARCHAR2(255), -- 요청사항
    company VARCHAR2(50), -- 택배사
    tracking_no VARCHAR2(100), -- 운송장 번호
    status VARCHAR2(50) NOT NULL, -- 배송 상태
    shipping_date TIMESTAMP, -- 발송일
    complete_date TIMESTAMP, -- 완료일
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- 코멘트
COMMENT ON TABLE delivery IS '배송 정보';
COMMENT ON COLUMN delivery.delivery_id IS '배송 번호';
COMMENT ON COLUMN delivery.order_id IS '주문 번호';
COMMENT ON COLUMN delivery.recipient IS '수령인';
COMMENT ON COLUMN delivery.tel IS '연락처';
COMMENT ON COLUMN delivery.zipcode IS '우편번호';
COMMENT ON COLUMN delivery.address1 IS '기본 주소';
COMMENT ON COLUMN delivery.address2 IS '상세 주소';
COMMENT ON COLUMN delivery.request IS '요청사항';
COMMENT ON COLUMN delivery.company IS '택배사';
COMMENT ON COLUMN delivery.tracking_no IS '운송장 번호';
COMMENT ON COLUMN delivery.status IS '배송 상태';
COMMENT ON COLUMN delivery.shipping_date IS '발송일';
COMMENT ON COLUMN delivery.complete_date IS '완료일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO delivery (delivery_id, order_id, recipient, tel, zipcode, address1, address2, request, company, tracking_no, status, shipping_date, complete_date) VALUES (delivery_seq.NEXTVAL, 1, '테스트_recipient_1', '테스트_tel_1', '06234', '서울시 강남구 역삼동', '123-45 2층', '테스트_request_1', '테스트_company_1', '테스트_tracking_no_1', '배송중', SYSDATE, SYSDATE);
INSERT INTO delivery (delivery_id, order_id, recipient, tel, zipcode, address1, address2, request, company, tracking_no, status, shipping_date, complete_date) VALUES (delivery_seq.NEXTVAL, 2, '테스트_recipient_2', '테스트_tel_2', '48012', '부산시 해운대구 우동', '래미안 아파트 101동 101호', '테스트_request_2', '테스트_company_2', '테스트_tracking_no_2', '배송완료', SYSDATE, SYSDATE);
INSERT INTO delivery (delivery_id, order_id, recipient, tel, zipcode, address1, address2, request, company, tracking_no, status, shipping_date, complete_date) VALUES (delivery_seq.NEXTVAL, 3, '테스트_recipient_3', '테스트_tel_3', '13524', '경기도 성남시 분당구 정자동', '푸르지오 202동 202호', '테스트_request_3', '테스트_company_3', '테스트_tracking_no_3', '배송준비중', SYSDATE, SYSDATE);
COMMIT;
