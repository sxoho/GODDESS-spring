-- ProductOption 테이블 및 시퀀스 생성

CREATE SEQUENCE product_option_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE product_option (
    product_option_id NUMBER PRIMARY KEY, -- 상품 옵션 번호 (기본키)
    product_id NUMBER NOT NULL, -- 상품 번호 (외래키, product 테이블 참조)
    product_color_id NUMBER NOT NULL, -- 상품 색상 번호 (외래키, product_color 테이블 참조)
    product_size_id NUMBER NOT NULL, -- 상품 사이즈 번호 (외래키, product_size 테이블 참조)
    add_price NUMBER DEFAULT 0, -- 추가 금액
    stock NUMBER DEFAULT 0, -- 재고 수량
    visible VARCHAR2(1) NOT NULL, -- 판매 여부(Y/N)
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (product_color_id) REFERENCES product_color(product_color_id),
    FOREIGN KEY (product_size_id) REFERENCES product_size(product_size_id)
);

-- 코멘트
COMMENT ON TABLE product_option IS '상품 옵션';
COMMENT ON COLUMN product_option.product_option_id IS '상품 옵션 번호';
COMMENT ON COLUMN product_option.product_id IS '상품 번호';
COMMENT ON COLUMN product_option.product_color_id IS '상품 색상 번호';
COMMENT ON COLUMN product_option.product_size_id IS '상품 사이즈 번호';
COMMENT ON COLUMN product_option.add_price IS '추가 금액';
COMMENT ON COLUMN product_option.stock IS '재고 수량';
COMMENT ON COLUMN product_option.visible IS '판매 여부(Y/N)';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO product_option (product_option_id, product_id, product_color_id, product_size_id, add_price, stock, visible) VALUES (product_option_seq.NEXTVAL, 1, 1, 1, 1, 10, 'Y');
INSERT INTO product_option (product_option_id, product_id, product_color_id, product_size_id, add_price, stock, visible) VALUES (product_option_seq.NEXTVAL, 2, 2, 2, 2, 50, 'Y');
INSERT INTO product_option (product_option_id, product_id, product_color_id, product_size_id, add_price, stock, visible) VALUES (product_option_seq.NEXTVAL, 3, 3, 3, 3, 30, 'Y');
COMMIT;
