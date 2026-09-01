-- Wishlist 테이블 및 시퀀스 생성

CREATE SEQUENCE wishlist_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE wishlist (
    wishlist_id NUMBER PRIMARY KEY, -- 찜 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    product_id NUMBER NOT NULL, -- 상품 번호 (외래키, product 테이블 참조)
    created_at TIMESTAMP DEFAULT SYSDATE, -- 등록일
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 코멘트
COMMENT ON TABLE wishlist IS '찜(위시리스트)';
COMMENT ON COLUMN wishlist.wishlist_id IS '찜 번호';
COMMENT ON COLUMN wishlist.member_id IS '회원 번호';
COMMENT ON COLUMN wishlist.product_id IS '상품 번호';
COMMENT ON COLUMN wishlist.created_at IS '등록일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO wishlist (wishlist_id, member_id, product_id, created_at) VALUES (wishlist_seq.NEXTVAL, 1, 1, SYSDATE);
INSERT INTO wishlist (wishlist_id, member_id, product_id, created_at) VALUES (wishlist_seq.NEXTVAL, 2, 2, SYSDATE);
INSERT INTO wishlist (wishlist_id, member_id, product_id, created_at) VALUES (wishlist_seq.NEXTVAL, 3, 3, SYSDATE);
COMMIT;

--중복방지 
ALTER TABLE wishlist
ADD CONSTRAINT uk_wishlist_member_product
UNIQUE (member_id, product_id);
