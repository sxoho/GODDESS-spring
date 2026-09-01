-- ProductColor 테이블 및 시퀀스 삭제
DROP TABLE product_color CASCADE CONSTRAINTS;
DROP SEQUENCE product_color_seq;

-- ProductColor 시퀀스 생성
CREATE SEQUENCE product_color_seq START WITH 1 INCREMENT BY 1 NOCACHE;

-- ProductColor 테이블 생성
CREATE TABLE product_color (
    product_color_id NUMBER PRIMARY KEY, -- 상품 색상 번호
    product_id NUMBER NOT NULL, -- 상품 번호(FK)
    color_id NUMBER NOT NULL, -- 색상 번호(FK)
    is_main VARCHAR2(1) NOT NULL, -- 대표 색상 여부(Y/N)
    sort_order NUMBER NOT NULL, -- 출력 순서
    fitting_enabled VARCHAR2(1) NOT NULL, -- 피팅 가능 여부(Y/N)
    visible VARCHAR2(1) NOT NULL, -- 사용 여부(Y/N)
    CONSTRAINT fk_product_color_product FOREIGN KEY (product_id) REFERENCES product(product_id),
    CONSTRAINT fk_product_color_info FOREIGN KEY (color_id) REFERENCES product_color_info(color_id),
    CONSTRAINT ck_product_color_main CHECK (is_main IN ('Y','N')),
    CONSTRAINT ck_product_color_fitting CHECK (fitting_enabled IN ('Y','N')),
    CONSTRAINT ck_product_color_visible CHECK (visible IN ('Y','N'))
);

-- 코멘트
COMMENT ON TABLE product_color IS '상품 색상';
COMMENT ON COLUMN product_color.product_color_id IS '상품 색상 번호';
COMMENT ON COLUMN product_color.product_id IS '상품 번호';
COMMENT ON COLUMN product_color.color_id IS '색상 번호';
COMMENT ON COLUMN product_color.is_main IS '대표 색상 여부(Y/N)';
COMMENT ON COLUMN product_color.sort_order IS '출력 순서';
COMMENT ON COLUMN product_color.fitting_enabled IS '피팅 가능 여부(Y/N)';
COMMENT ON COLUMN product_color.visible IS '사용 여부(Y/N)';

-- 등록
INSERT INTO product_color(product_color_id, product_id, color_id, is_main, sort_order, fitting_enabled, visible)
VALUES(product_color_seq.NEXTVAL, 1, 1, 'Y', 1, 'Y', 'Y');

COMMIT;

-- 글로니 베이직 반팔 색상 등록(test)
INSERT INTO product_color(product_color_id, product_id, color_id, is_main, sort_order, fitting_enabled, visible)
VALUES(product_color_seq.NEXTVAL, (SELECT product_id FROM product WHERE name='글로니 베이직 반팔'), (SELECT color_id FROM product_color_info WHERE color_name='블랙'), 'Y', 1, 'Y', 'Y');

INSERT INTO product_color(product_color_id, product_id, color_id, is_main, sort_order, fitting_enabled, visible)
VALUES(product_color_seq.NEXTVAL, (SELECT product_id FROM product WHERE name='글로니 베이직 반팔'), (SELECT color_id FROM product_color_info WHERE color_name='화이트'), 'N', 2, 'Y', 'Y');

INSERT INTO product_color(product_color_id, product_id, color_id, is_main, sort_order, fitting_enabled, visible)
VALUES(product_color_seq.NEXTVAL, (SELECT product_id FROM product WHERE name='글로니 베이직 반팔'), (SELECT color_id FROM product_color_info WHERE color_name='그레이'), 'N', 3, 'Y', 'Y');

COMMIT;

--글리터 셔츠 등록
INSERT INTO product_color(product_color_id, product_id, color_id, is_main, sort_order, fitting_enabled, visible) 
VALUES(product_color_seq.NEXTVAL, (SELECT product_id FROM product WHERE name='글리터 셔츠'), (SELECT color_id FROM product_color_info WHERE color_name='화이트'), 'Y', 1, 'Y', 'Y');
COMMIT;

-- 전체 조회
select * from PRODUCT_COLOR;
SELECT * FROM product_color ORDER BY product_color_id;

-- 상품별 색상 조회
SELECT pc.product_color_id, pc.product_id, pc.color_id, pcm.color_name, pcm.color_code, pc.is_main, pc.sort_order, pc.fitting_enabled, pc.visible
FROM product_color pc
JOIN product_color_info pcm ON pc.color_id = pcm.color_id
WHERE pc.product_id = 1
ORDER BY pc.sort_order;

-- 수정
UPDATE product_color
SET color_id = 2, is_main = 'N', sort_order = 2, fitting_enabled = 'Y', visible = 'Y'
WHERE product_color_id = 1;

COMMIT;

-- 삭제
DELETE FROM product_color
WHERE product_color_id = 1;

COMMIT;