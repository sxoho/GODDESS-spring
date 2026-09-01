-- QuickFitting 테이블 및 시퀀스 생성

CREATE SEQUENCE quick_fitting_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE quick_fitting (
    quick_fitting_id NUMBER PRIMARY KEY, -- 간편 피팅 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    member_photo_id NUMBER NOT NULL, -- 회원 사진 번호 (외래키, member_photo 테이블 참조)
    product_id NUMBER NOT NULL, -- 상품 번호 (외래키, product 테이블 참조)
    status VARCHAR2(20) NOT NULL, -- 처리 상태(PENDING/PROCESSING/SUCCESS/FAILED)
    error_message CLOB, -- 오류 메시지
    created_at TIMESTAMP DEFAULT SYSDATE, -- 최초 생성일
    last_view_date TIMESTAMP, -- 마지막 조회일
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (member_photo_id) REFERENCES member_photo(member_photo_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 코멘트
COMMENT ON TABLE quick_fitting IS '간편 피팅 요청';
COMMENT ON COLUMN quick_fitting.quick_fitting_id IS '간편 피팅 번호';
COMMENT ON COLUMN quick_fitting.member_id IS '회원 번호';
COMMENT ON COLUMN quick_fitting.member_photo_id IS '회원 사진 번호';
COMMENT ON COLUMN quick_fitting.product_id IS '상품 번호';
COMMENT ON COLUMN quick_fitting.status IS '처리 상태(PENDING/PROCESSING/SUCCESS/FAILED)';
COMMENT ON COLUMN quick_fitting.error_message IS '오류 메시지';
COMMENT ON COLUMN quick_fitting.created_at IS '최초 생성일';
COMMENT ON COLUMN quick_fitting.last_view_date IS '마지막 조회일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO quick_fitting (quick_fitting_id, member_id, member_photo_id, product_id, status, error_message, created_at, last_view_date) VALUES (quick_fitting_seq.NEXTVAL, 1, 1, 1, '테스트_status_1', '테스트_error_message_1', SYSDATE, SYSDATE);
INSERT INTO quick_fitting (quick_fitting_id, member_id, member_photo_id, product_id, status, error_message, created_at, last_view_date) VALUES (quick_fitting_seq.NEXTVAL, 2, 2, 2, '테스트_status_2', '테스트_error_message_2', SYSDATE, SYSDATE);
INSERT INTO quick_fitting (quick_fitting_id, member_id, member_photo_id, product_id, status, error_message, created_at, last_view_date) VALUES (quick_fitting_seq.NEXTVAL, 3, 3, 3, '테스트_status_3', '테스트_error_message_3', SYSDATE, SYSDATE);
COMMIT;
