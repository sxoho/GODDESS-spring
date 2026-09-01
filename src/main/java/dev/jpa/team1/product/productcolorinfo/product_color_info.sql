-- ProductColorinfo 테이블 및 시퀀스 삭제
DROP TABLE product_color_info CASCADE CONSTRAINTS;
DROP SEQUENCE product_color_info_seq;

-- ProductColorinfo 시퀀스 생성
CREATE SEQUENCE product_color_info_seq START WITH 1 INCREMENT BY 1 NOCACHE;

-- ProductColorinfo 테이블 생성
CREATE TABLE product_color_info (
    color_id NUMBER PRIMARY KEY, -- 색상 번호
    color_name VARCHAR2(50) NOT NULL, -- 색상명
    color_code VARCHAR2(20) NOT NULL -- 색상 코드
);

-- 코멘트
COMMENT ON TABLE product_color_info IS '상품 색상 정보';
COMMENT ON COLUMN product_color_info.color_id IS '색상 번호';
COMMENT ON COLUMN product_color_info.color_name IS '색상명';
COMMENT ON COLUMN product_color_info.color_code IS '색상 코드';

-- 등록
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '블랙', '#000000');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '화이트', '#FFFFFF');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '그레이', '#808080');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '네이비', '#001F54');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '블루', '#0057B8');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '베이지', '#F5F5DC');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '브라운', '#8B4513');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '카키', '#6B8E23');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '그린', '#228B22');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '핑크', '#FFC0CB');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '레드', '#FF0000');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '옐로우', '#FFD700');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '오렌지', '#FF8C00');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '퍼플', '#800080');
INSERT INTO product_color_info VALUES(product_color_info_seq.NEXTVAL, '아이보리', '#FFFFF0');

COMMIT;

-- 전체 조회
SELECT * FROM product_color_info ORDER BY color_id;

-- 상세 조회
SELECT * FROM product_color_info WHERE color_id = 1;

-- 수정
UPDATE product_color_info
SET color_name = '다크네이비',
    color_code = '#000080'
WHERE color_id = 4;

COMMIT;

-- 삭제
DELETE FROM product_color_info
WHERE color_id = 15;

COMMIT;