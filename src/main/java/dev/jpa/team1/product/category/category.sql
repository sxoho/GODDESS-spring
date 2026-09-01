DROP TABLE category CASCADE CONSTRAINTS;
DROP SEQUENCE category_seq;

-- CATEGORY 시퀀스 및 테이블 생성
CREATE SEQUENCE category_seq START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE category (
    category_id NUMBER PRIMARY KEY,
    parent_id NUMBER,
    name VARCHAR2(100) NOT NULL,
    description VARCHAR2(255),
    sort_order NUMBER NOT NULL,
    visible VARCHAR2(1) DEFAULT 'Y' NOT NULL,
    created_at TIMESTAMP DEFAULT SYSDATE,
    CONSTRAINT fk_category_parent
        FOREIGN KEY (parent_id) REFERENCES category(category_id),
    CONSTRAINT ck_category_visible
        CHECK (visible IN ('Y', 'N'))
);

COMMENT ON TABLE category IS '상품 카테고리';
COMMENT ON COLUMN category.category_id IS '카테고리 번호';
COMMENT ON COLUMN category.parent_id IS '상위 카테고리 번호';
COMMENT ON COLUMN category.name IS '카테고리명';
COMMENT ON COLUMN category.description IS '카테고리 설명';
COMMENT ON COLUMN category.sort_order IS '출력 순서';
COMMENT ON COLUMN category.visible IS '사용 여부(Y/N)';
COMMENT ON COLUMN category.created_at IS '등록일';

-- 대분류 등록
INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL, NULL, '여성', '여성 상품 카테고리', 1, 'Y');

INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL, NULL, '남성', '남성 상품 카테고리', 2, 'Y');

INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL, NULL, '액세서리', '액세서리 상품 카테고리', 3, 'Y');

-- 하위 카테고리 등록
INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL,(SELECT category_id FROM category WHERE name = '여성' AND parent_id IS NULL),'상의','여성 상의 상품', 1, 'Y');

INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL,(SELECT category_id FROM category WHERE name = '여성' AND parent_id IS NULL),'하의','여성 하의 상품', 2, 'Y');

COMMIT;

-- 남성 하위 카테고리
INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL, (SELECT category_id FROM category WHERE name='남성' AND parent_id IS NULL), '상의', '남성 상의 상품', 1, 'Y');

INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL, (SELECT category_id FROM category WHERE name='남성' AND parent_id IS NULL), '하의', '남성 하의 상품', 2, 'Y');

-- 액세서리 하위 카테고리
INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL, (SELECT category_id FROM category WHERE name='액세서리' AND parent_id IS NULL), '신발', '신발 상품', 1, 'Y');

INSERT INTO category(category_id, parent_id, name, description, sort_order, visible)
VALUES(category_seq.NEXTVAL, (SELECT category_id FROM category WHERE name='액세서리' AND parent_id IS NULL), '모자', '모자 상품', 2, 'Y');

COMMIT;

-- 전체 목록
SELECT category_id, parent_id, name, description,
       sort_order, visible, created_at
FROM category
ORDER BY parent_id NULLS FIRST, sort_order, category_id;

-- 상세 조회
SELECT category_id, parent_id, name, description,
       sort_order, visible, created_at
FROM category
WHERE category_id = 1;

-- 수정
UPDATE category
SET name = '아우터',
    description = '재킷과 코트 상품',
    sort_order = 3,
    visible = 'Y'
WHERE category_id = 1;

COMMIT;

-- 비활성화
UPDATE category
SET visible = 'N'
WHERE category_id = 1;

COMMIT;

-- 삭제
DELETE FROM category
WHERE category_id = 1;

COMMIT;