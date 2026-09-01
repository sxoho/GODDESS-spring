-- OrderDetail 테이블 및 시퀀스 생성

CREATE SEQUENCE order_detail_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE order_detail (
    order_detail_id NUMBER PRIMARY KEY, -- 주문 상세 번호 (기본키)
    order_id NUMBER NOT NULL, -- 주문 번호 (외래키, orders 테이블 참조)
    product_option_id NUMBER NOT NULL, -- 상품 옵션 번호 (외래키, product_option 테이블 참조)
    quick_fitting_result_id NUMBER, -- 간편 피팅 결과 번호 (외래키, quick_fitting_result 테이블 참조)
    coord_fitting_result_id NUMBER, -- 코디 피팅 결과 번호 (외래키, coord_fitting_result 테이블 참조)
    product_name VARCHAR2(255) NOT NULL, -- 주문 당시 상품명
    color_name VARCHAR2(50) NOT NULL, -- 주문 당시 색상명
    size_name VARCHAR2(50) NOT NULL, -- 주문 당시 사이즈명
    price NUMBER NOT NULL, -- 주문 당시 가격
    quantity NUMBER NOT NULL, -- 수량
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_option_id) REFERENCES product_option(product_option_id),
    FOREIGN KEY (quick_fitting_result_id) REFERENCES quick_fitting_result(quick_fitting_result_id),
    FOREIGN KEY (coord_fitting_result_id) REFERENCES coord_fitting_result(coord_fitting_result_id)
);

-- 코멘트
COMMENT ON TABLE order_detail IS '주문 상세';
COMMENT ON COLUMN order_detail.order_detail_id IS '주문 상세 번호';
COMMENT ON COLUMN order_detail.order_id IS '주문 번호';
COMMENT ON COLUMN order_detail.product_option_id IS '상품 옵션 번호';
COMMENT ON COLUMN order_detail.quick_fitting_result_id IS '간편 피팅 결과 번호';
COMMENT ON COLUMN order_detail.coord_fitting_result_id IS '코디 피팅 결과 번호';
COMMENT ON COLUMN order_detail.product_name IS '주문 당시 상품명';
COMMENT ON COLUMN order_detail.color_name IS '주문 당시 색상명';
COMMENT ON COLUMN order_detail.size_name IS '주문 당시 사이즈명';
COMMENT ON COLUMN order_detail.price IS '주문 당시 가격';
COMMENT ON COLUMN order_detail.quantity IS '수량';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO order_detail (order_detail_id, order_id, product_option_id, quick_fitting_result_id, coord_fitting_result_id, product_name, color_name, size_name, price, quantity) VALUES (order_detail_seq.NEXTVAL, 1, 1, 1, 1, '테스트_product_name_1', '테스트_color_name_1', '테스트_size_name_1', 35000, 1);
INSERT INTO order_detail (order_detail_id, order_id, product_option_id, quick_fitting_result_id, coord_fitting_result_id, product_name, color_name, size_name, price, quantity) VALUES (order_detail_seq.NEXTVAL, 2, 2, 2, 2, '테스트_product_name_2', '테스트_color_name_2', '테스트_size_name_2', 39900, 1);
INSERT INTO order_detail (order_detail_id, order_id, product_option_id, quick_fitting_result_id, coord_fitting_result_id, product_name, color_name, size_name, price, quantity) VALUES (order_detail_seq.NEXTVAL, 3, 3, 3, 3, '테스트_product_name_3', '테스트_color_name_3', '테스트_size_name_3', 9900, 3);
COMMIT;
