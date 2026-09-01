DROP TABLE product CASCADE CONSTRAINTS;
DROP SEQUENCE product_seq;

CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE product (
    product_id NUMBER PRIMARY KEY,
    category_id NUMBER NOT NULL,
    
    brand VARCHAR2(100) NOT NULL, 
    name VARCHAR2(255) NOT NULL,
    description CLOB,
    price NUMBER NOT NULL,
    discount VARCHAR2(50),
    material VARCHAR2(255),
    washing VARCHAR2(255),
    fitting_enabled VARCHAR2(1) NOT NULL,
    status VARCHAR2(20) NOT NULL,
    view_count NUMBER DEFAULT 0,
    created_at TIMESTAMP DEFAULT SYSDATE,
    updated_at TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY(category_id) REFERENCES category(category_id),
    CONSTRAINT ck_product_fitting CHECK(fitting_enabled IN ('Y', 'N')),
    CONSTRAINT ck_product_status CHECK(status IN ('판매중', '품절', '판매중지'))
);

COMMENT ON TABLE product IS '상품';
COMMENT ON COLUMN product.product_id IS '상품 번호';
COMMENT ON COLUMN product.category_id IS '카테고리 번호';
COMMENT ON COLUMN product.brand IS '브랜드명';
COMMENT ON COLUMN product.name IS '상품명';
COMMENT ON COLUMN product.description IS '상품 설명';
COMMENT ON COLUMN product.price IS '기본 가격';
COMMENT ON COLUMN product.discount IS '할인 가격/율';
COMMENT ON COLUMN product.material IS '소재';
COMMENT ON COLUMN product.washing IS '세탁 정보';
COMMENT ON COLUMN product.fitting_enabled IS '피팅 가능 여부(Y/N)';
COMMENT ON COLUMN product.status IS '판매 상태';
COMMENT ON COLUMN product.view_count IS '조회수';
COMMENT ON COLUMN product.created_at IS '등록일';
COMMENT ON COLUMN product.updated_at IS '수정일';

-- 등록
INSERT INTO product(product_id, category_id, brand, name, description, price, discount, material, washing, fitting_enabled, status)
 VALUES(product_seq.NEXTVAL, 4, 'ONNIVER BASIC', '베이직 반팔 티셔츠', '데일리 티셔츠', 29000, '10%', '면100%', '찬물 세탁', 'Y', '판매중');

 INSERT INTO product(product_id, category_id, brand, name, description, price, discount, material, washing, fitting_enabled, status)
VALUES(
    product_seq.NEXTVAL,
    (SELECT category_id FROM category WHERE name='상의' AND parent_id=(SELECT category_id FROM category WHERE name='여성' AND parent_id IS NULL)),
    'GLOWNY',
    '글로니 베이직 반팔',
    '부드러운 코튼 베이직 반팔 티셔츠',
    39000,
    '10%',
    '면 100%',
    '찬물 세탁',
    'Y',
    '판매중'
);

-- 글리터 셔츠 
INSERT INTO product(product_id, category_id, brand, name, description, price, discount, material, washing, fitting_enabled, status)
VALUES(
    product_seq.NEXTVAL, (SELECT category_id FROM category WHERE name='상의' AND parent_id=(SELECT category_id FROM category WHERE name='여성' AND parent_id IS NULL)),
 'GLITTER', '글리터 셔츠', '은은한 광택이 돋보이는 오버핏 셔츠', 59000, '15%', '폴리에스터 65%, 면 35%', '단독 손세탁 권장', 'Y', '판매중');

COMMIT;


COMMIT;

-- 전체 조회
SELECT * FROM product ORDER BY product_id;

-- 상세 조회
SELECT * FROM product WHERE product_id = 1;

-- 카테고리별 조회
SELECT * FROM product WHERE category_id = 4;

-- 상품명 검색
SELECT * FROM product WHERE name LIKE '%티셔츠%';

-- 수정
UPDATE product
SET name = '오버핏 반팔 티셔츠',
    price = 32000,
    discount = '15%',
    status = '판매중',
    updated_at = SYSDATE
WHERE product_id = 1;

-- 이름 수정
UPDATE product
SET name = '글로니 베이직 반팔 3color'
WHERE product_id = 2;

COMMIT;

COMMIT;

-- 조회수 증가
UPDATE product
SET view_count = view_count + 1
WHERE product_id = 1;

COMMIT;

-- 삭제
DELETE FROM product WHERE product_id = 1;

COMMIT;