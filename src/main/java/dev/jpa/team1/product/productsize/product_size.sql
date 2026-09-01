-- ProductSize 테이블 및 시퀀스 생성

CREATE SEQUENCE product_size_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE product_size (
    product_size_id NUMBER PRIMARY KEY, -- 상품 사이즈 번호 (기본키)
    product_id NUMBER NOT NULL, -- 상품 번호 (외래키, product 테이블 참조)
    size_name VARCHAR2(50) NOT NULL, -- 사이즈명
    size_description VARCHAR2(255), -- 사이즈 설명
    visible VARCHAR2(1) NOT NULL, -- 사용 여부(Y/N)
    sort_order NUMBER NOT NULL, -- 출력 순서
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 코멘트
COMMENT ON TABLE product_size IS '상품 사이즈';
COMMENT ON COLUMN product_size.product_size_id IS '상품 사이즈 번호';
COMMENT ON COLUMN product_size.product_id IS '상품 번호';
COMMENT ON COLUMN product_size.size_name IS '사이즈명';
COMMENT ON COLUMN product_size.size_description IS '사이즈 설명';
COMMENT ON COLUMN product_size.visible IS '사용 여부(Y/N)';
COMMENT ON COLUMN product_size.sort_order IS '출력 순서';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO product_size (product_size_id, product_id, size_name, size_description, visible, sort_order) VALUES (product_size_seq.NEXTVAL, 1, 'M', '테스트_size_description_1', 'Y', 1);
INSERT INTO product_size (product_size_id, product_id, size_name, size_description, visible, sort_order) VALUES (product_size_seq.NEXTVAL, 2, 'L', '테스트_size_description_2', 'Y', 2);
INSERT INTO product_size (product_size_id, product_id, size_name, size_description, visible, sort_order) VALUES (product_size_seq.NEXTVAL, 3, 'XL', '테스트_size_description_3', 'Y', 3);
COMMIT;
