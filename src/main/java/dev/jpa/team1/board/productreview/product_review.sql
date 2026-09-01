-- 기존 테이블 및 시퀀스 삭제 
DROP TABLE product_review CASCADE CONSTRAINTS; 
DROP SEQUENCE product_review_seq; 
 
 
-- ProductReview 테이블 및 시퀀스 생성 
CREATE SEQUENCE product_review_seq START WITH 1 INCREMENT BY 1; 
 
CREATE TABLE product_review ( 
    review_id NUMBER PRIMARY KEY, -- 후기 번호 
    member_id NUMBER NOT NULL, -- 회원 번호 
    order_detail_id NUMBER, -- 주문 상세 번호 (주문 기능 구현 전 NULL 허용) -> 구현하면 NOT NULL 
    product_id NUMBER NOT NULL, -- 상품 번호 
    fitting_save_id NUMBER, -- 저장한 AI 피팅 번호 (선택) 
    title VARCHAR2(255) NOT NULL, -- 제목 
    content CLOB NOT NULL, -- 내용 
    rating NUMBER NOT NULL, -- 별점(1~5) 
    created_at TIMESTAMP DEFAULT SYSDATE, -- 등록일 
    updated_at TIMESTAMP, -- 수정일 
    visible VARCHAR2(1) DEFAULT 'Y' NOT NULL, -- 공개 여부(Y/N) 
 
    -- 외래키 
    FOREIGN KEY (member_id) REFERENCES member(member_id), -- 회원 
    FOREIGN KEY (order_detail_id) REFERENCES order_detail(order_detail_id), -- 주문 상세 
    FOREIGN KEY (product_id) REFERENCES product(product_id), -- 상품 
    FOREIGN KEY (fitting_save_id) REFERENCES fitting_save(fitting_save_id), -- AI 피팅 
 
    -- 제약조건 
    CONSTRAINT chk_product_review_rating CHECK (rating BETWEEN 1 AND 5), -- 별점 1~5 
    CONSTRAINT chk_product_review_visible CHECK (visible IN ('Y', 'N')) -- 공개 여부 
); 
 
 
-- 코멘트 
COMMENT ON TABLE product_review IS '상품 후기'; 
COMMENT ON COLUMN product_review.review_id IS '후기 번호'; 
COMMENT ON COLUMN product_review.member_id IS '회원 번호'; 
COMMENT ON COLUMN product_review.order_detail_id IS '주문 상세 번호'; 
COMMENT ON COLUMN product_review.product_id IS '상품 번호'; 
COMMENT ON COLUMN product_review.fitting_save_id IS '저장한 피팅 번호 (AI 비교용)'; 
COMMENT ON COLUMN product_review.title IS '제목'; 
COMMENT ON COLUMN product_review.content IS '내용'; 
COMMENT ON COLUMN product_review.rating IS '별점'; 
COMMENT ON COLUMN product_review.created_at IS '등록일'; 
COMMENT ON COLUMN product_review.updated_at IS '수정일'; 
COMMENT ON COLUMN product_review.visible IS '공개 여부(Y/N)'; 
 
 
-- 예시 데이터 
-- 주문/AI 피팅 기능 구현 전이라 order_detail_id, fitting_save_id는 NULL로 테스트 
INSERT INTO product_review 
(review_id, member_id, order_detail_id, product_id, fitting_save_id, title, content, rating, created_at, updated_at, visible) 
VALUES 
(product_review_seq.NEXTVAL, 8, NULL, 17, NULL, '완전 만족해요!', '옷감도 좋고 핏이 예술입니다. 다른 색상도 살까 고민중이에요.', 5, SYSDATE, NULL, 'Y'); 
 
INSERT INTO product_review 
(review_id, member_id, order_detail_id, product_id, fitting_save_id, title, content, rating, created_at, updated_at, visible) 
VALUES 
(product_review_seq.NEXTVAL, 8, NULL, 21, NULL, '가성비 최고', '이 가격에 이 퀄리티라니 믿기지 않네요. 배송도 엄청 빨랐습니다.', 4, SYSDATE, NULL, 'Y'); 
 
INSERT INTO product_review 
(review_id, member_id, order_detail_id, product_id, fitting_save_id, title, content, rating, created_at, updated_at, visible) 
VALUES 
(product_review_seq.NEXTVAL, 8, NULL, 27, NULL, '조금 크네요', '한 치수 작게 살 걸 그랬어요. 그래도 넉넉하게 입으려구요.', 3, SYSDATE, NULL, 'Y'); 
 
COMMIT; 
 
 
-- 데이터 확인 
SELECT * FROM product_review ORDER BY review_id DESC; 
 
select product_id from PRODUCT; 
 
select * from MEMBER; 
 
SELECT 
    c.table_name, 
    c.constraint_type, 
    cc.column_name 
FROM user_constraints c 
JOIN user_cons_columns cc 
    ON c.constraint_name = cc.constraint_name 
WHERE c.table_name IN ( 
    'MEMBER', 
    'ORDER_DETAIL', 
    'PRODUCT', 
    'FITTING_SAVE' 
) 
AND c.constraint_type IN ('P', 'U') 
ORDER BY c.table_name;