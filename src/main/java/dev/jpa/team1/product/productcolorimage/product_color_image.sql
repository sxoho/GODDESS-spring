-- 기존 삭제
DROP TABLE product_color_image CASCADE CONSTRAINTS;
DROP SEQUENCE color_image_seq;

-- 시퀀스 생성
CREATE SEQUENCE color_image_seq START WITH 1 INCREMENT BY 1 NOCACHE;

-- 테이블 생성
CREATE TABLE product_color_image (
    color_image_id NUMBER PRIMARY KEY,
    product_color_id NUMBER NOT NULL,
    file_origin VARCHAR2(255) NOT NULL,
    file_saved VARCHAR2(255) NOT NULL,
    file_path VARCHAR2(500) NOT NULL,
    is_main VARCHAR2(1) NOT NULL,
    image_type VARCHAR2(50) NOT NULL,
    sort_order NUMBER NOT NULL,
    created_at TIMESTAMP DEFAULT SYSDATE,

    CONSTRAINT fk_product_color_image
        FOREIGN KEY (product_color_id)
        REFERENCES product_color(product_color_id),

    CONSTRAINT ck_product_color_image_main
        CHECK (is_main IN ('Y','N'))
);

-- 코멘트
COMMENT ON TABLE product_color_image IS '상품 색상 이미지';
COMMENT ON COLUMN product_color_image.color_image_id IS '색상 이미지 번호';
COMMENT ON COLUMN product_color_image.product_color_id IS '상품 색상 번호';
COMMENT ON COLUMN product_color_image.file_origin IS '원본 파일명';
COMMENT ON COLUMN product_color_image.file_saved IS '저장 파일명';
COMMENT ON COLUMN product_color_image.file_path IS '저장 경로';
COMMENT ON COLUMN product_color_image.is_main IS '대표 이미지 여부(Y/N)';
COMMENT ON COLUMN product_color_image.image_type IS '이미지 종류';
COMMENT ON COLUMN product_color_image.sort_order IS '출력 순서';
COMMENT ON COLUMN product_color_image.created_at IS '등록일';

-- 등록
-- INSERT INTO product_color_image(
--     color_image_id,
--     product_color_id,
--     file_origin,
--     file_saved,
--     file_path,
--     is_main,
--     image_type,
--     sort_order
-- )
-- VALUES(
--     color_image_seq.NEXTVAL,
--     1,
--     'glowny_black_01.webp',
--     'glowny_black_01.webp',
--     '/images/product/glowny/glowny_black_01.webp',
--     'Y',
--     'DETAIL',
--     1
-- );

 COMMIT;

-- 글로니 반팔 블랙 대표 이미지
INSERT INTO product_color_image(color_image_id, product_color_id, file_origin, file_saved, file_path, is_main, image_type, sort_order)
VALUES(color_image_seq.NEXTVAL, (SELECT pc.product_color_id FROM product_color pc JOIN product p ON pc.product_id=p.product_id JOIN product_color_info pcm ON pc.color_id=pcm.color_id WHERE p.name='글로니 베이직 반팔' AND pcm.color_name='블랙'), 'glowny_black_01.webp', 'glowny_black_01.webp', '/images/product/glowny/glowny_black_01.webp', 'Y', 'MAIN', 1);

-- 블랙 추가 이미지
INSERT INTO product_color_image(color_image_id, product_color_id, file_origin, file_saved, file_path, is_main, image_type, sort_order)
VALUES(color_image_seq.NEXTVAL, (SELECT pc.product_color_id FROM product_color pc JOIN product p ON pc.product_id=p.product_id JOIN product_color_info pcm ON pc.color_id=pcm.color_id WHERE p.name='글로니 베이직 반팔' AND pcm.color_name='블랙'), 'glowny_black_02.webp', 'glowny_black_02.webp', '/images/product/glowny/glowny_black_02.webp', 'N', 'DETAIL', 2);

-- 화이트 대표 이미지
INSERT INTO product_color_image(color_image_id, product_color_id, file_origin, file_saved, file_path, is_main, image_type, sort_order)
VALUES(color_image_seq.NEXTVAL, (SELECT pc.product_color_id FROM product_color pc JOIN product p ON pc.product_id=p.product_id JOIN product_color_info pcm ON pc.color_id=pcm.color_id WHERE p.name='글로니 베이직 반팔' AND pcm.color_name='화이트'), 'glowny_white_01.webp', 'glowny_white_01.webp', '/images/product/glowny/glowny_white_01.webp', 'Y', 'MAIN', 1);

-- 그레이 대표 이미지
INSERT INTO product_color_image(color_image_id, product_color_id, file_origin, file_saved, file_path, is_main, image_type, sort_order)
VALUES(color_image_seq.NEXTVAL, (SELECT pc.product_color_id FROM product_color pc JOIN product p ON pc.product_id=p.product_id JOIN product_color_info pcm ON pc.color_id=pcm.color_id WHERE p.name='글로니 베이직 반팔' AND pcm.color_name='그레이'), 'glowny_gray_01.webp', 'glowny_gray_01.webp', '/images/product/glowny/glowny_gray_01.webp', 'Y', 'MAIN', 1);

COMMIT;

--글리터 셔츠 화이트 이미지 
INSERT INTO product_color_image(color_image_id, product_color_id, file_origin, file_saved, file_path, is_main, image_type, sort_order) 
VALUES(color_image_seq.NEXTVAL, (SELECT pc.product_color_id FROM product_color pc JOIN product p ON pc.product_id=p.product_id JOIN product_color_info pcm ON pc.color_id=pcm.color_id WHERE p.name='글리터 셔츠' AND pcm.color_name='화이트'), 'glitter_white_01.webp', 'glitter_white_01.webp', '/images/product/glitter/glitter_white_01.webp', 'Y', 'MAIN', 1);
COMMIT;

-- 전체 조회
SELECT *
FROM product_color_image
ORDER BY color_image_id;

-- 상품 색상별 조회
SELECT *
FROM product_color_image
WHERE product_color_id = 1
ORDER BY sort_order;

-- 수정
UPDATE product_color_image
SET file_origin = 'glowny_black_02.webp',
    file_saved = 'glowny_black_02.jpg',
    file_path = '/images/product/glowny/glowny_black_02.jpg',
    is_main = 'N',
    image_type = 'DETAIL',
    sort_order = 2
WHERE color_image_id = 1;

COMMIT;

-- 삭제
DELETE FROM product_color_image
WHERE color_image_id = 1;

COMMIT;