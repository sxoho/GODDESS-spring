-- CoordFittingItem 테이블 및 시퀀스 생성

CREATE SEQUENCE coord_fitting_item_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE coord_fitting_item (
    coord_fitting_item_id NUMBER PRIMARY KEY, -- 코디 피팅 상품 번호 (기본키)
    coord_fitting_id NUMBER NOT NULL, -- 코디 피팅 번호 (외래키, coord_fitting 테이블 참조)
    product_color_id NUMBER, -- 상품 색상 번호 (외래키, product_color 테이블 참조)
    product_option_id NUMBER, -- 상품 옵션 번호 (외래키, product_option 테이블 참조)
    item_type VARCHAR2(50) NOT NULL, -- 상품 역할(상의, 하의 등)
    sort_order NUMBER NOT NULL, -- 적용 순서
    FOREIGN KEY (coord_fitting_id) REFERENCES coord_fitting(coord_fitting_id),
    FOREIGN KEY (product_color_id) REFERENCES product_color(product_color_id),
    FOREIGN KEY (product_option_id) REFERENCES product_option(product_option_id)
);

-- 코멘트
COMMENT ON TABLE coord_fitting_item IS '코디 피팅 선택 상품';
COMMENT ON COLUMN coord_fitting_item.coord_fitting_item_id IS '코디 피팅 상품 번호';
COMMENT ON COLUMN coord_fitting_item.coord_fitting_id IS '코디 피팅 번호';
COMMENT ON COLUMN coord_fitting_item.product_color_id IS '상품 색상 번호';
COMMENT ON COLUMN coord_fitting_item.product_option_id IS '상품 옵션 번호';
COMMENT ON COLUMN coord_fitting_item.item_type IS '상품 역할(상의, 하의 등)';
COMMENT ON COLUMN coord_fitting_item.sort_order IS '적용 순서';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO coord_fitting_item (coord_fitting_item_id, coord_fitting_id, product_color_id, product_option_id, item_type, sort_order) VALUES (coord_fitting_item_seq.NEXTVAL, 1, 1, 1, '테스트_item_type_1', 1);
INSERT INTO coord_fitting_item (coord_fitting_item_id, coord_fitting_id, product_color_id, product_option_id, item_type, sort_order) VALUES (coord_fitting_item_seq.NEXTVAL, 2, 2, 2, '테스트_item_type_2', 2);
INSERT INTO coord_fitting_item (coord_fitting_item_id, coord_fitting_id, product_color_id, product_option_id, item_type, sort_order) VALUES (coord_fitting_item_seq.NEXTVAL, 3, 3, 3, '테스트_item_type_3', 3);
COMMIT;
