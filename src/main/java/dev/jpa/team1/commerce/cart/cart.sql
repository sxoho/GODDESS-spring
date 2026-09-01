-- Cart 테이블 및 시퀀스 생성

CREATE SEQUENCE cart_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE cart (
    cart_id NUMBER PRIMARY KEY, -- 장바구니 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    product_option_id NUMBER NOT NULL, -- 상품 옵션 번호 (외래키, product_option 테이블 참조)
    quick_fitting_result_id NUMBER, -- 간편 피팅 결과 번호(AI 전환 추적) (외래키, quick_fitting_result 테이블 참조)
    coord_fitting_result_id NUMBER, -- 코디 피팅 결과 번호(AI 전환 추적) (외래키, coord_fitting_result 테이블 참조)
    quantity NUMBER NOT NULL, -- 수량
    file_path VARCHAR2(500), -- 상품/피팅 이미지 파일 경로
    created_at TIMESTAMP DEFAULT SYSDATE, -- 등록일
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (product_option_id) REFERENCES product_option(product_option_id),
    FOREIGN KEY (quick_fitting_result_id) REFERENCES quick_fitting_result(quick_fitting_result_id),
    FOREIGN KEY (coord_fitting_result_id) REFERENCES coord_fitting_result(coord_fitting_result_id)
);

-- 코멘트
COMMENT ON TABLE cart IS '장바구니';
COMMENT ON COLUMN cart.cart_id IS '장바구니 번호';
COMMENT ON COLUMN cart.member_id IS '회원 번호';
COMMENT ON COLUMN cart.product_option_id IS '상품 옵션 번호';
COMMENT ON COLUMN cart.quick_fitting_result_id IS '간편 피팅 결과 번호(AI 전환 추적)';
COMMENT ON COLUMN cart.coord_fitting_result_id IS '코디 피팅 결과 번호(AI 전환 추적)';
COMMENT ON COLUMN cart.quantity IS '수량';
COMMENT ON COLUMN cart.file_path IS '이미지 파일 경로';
COMMENT ON COLUMN cart.created_at IS '등록일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO cart (cart_id, member_id, product_option_id, quick_fitting_result_id, coord_fitting_result_id, quantity, file_path, created_at) VALUES (cart_seq.NEXTVAL, 1, 1, 1, 1, 2, '/uploads/product/sample1.jpg', SYSDATE);
INSERT INTO cart (cart_id, member_id, product_option_id, quick_fitting_result_id, coord_fitting_result_id, quantity, file_path, created_at) VALUES (cart_seq.NEXTVAL, 2, 2, 2, 2, 1, '/uploads/product/sample2.jpg', SYSDATE);
INSERT INTO cart (cart_id, member_id, product_option_id, quick_fitting_result_id, coord_fitting_result_id, quantity, file_path, created_at) VALUES (cart_seq.NEXTVAL, 3, 3, 3, 3, 3, '/uploads/product/sample3.jpg', SYSDATE);
COMMIT;
