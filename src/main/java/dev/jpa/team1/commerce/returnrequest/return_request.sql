-- ReturnRequest 테이블 및 시퀀스 생성

CREATE SEQUENCE return_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE return_request (
    return_id NUMBER PRIMARY KEY, -- 반품 번호 (기본키)
    order_detail_id NUMBER NOT NULL, -- 주문 상세 번호 (외래키, order_detail 테이블 참조)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    reason_type VARCHAR2(50) NOT NULL, -- 반품 사유 구분
    reason CLOB, -- 상세 사유
    status VARCHAR2(50) NOT NULL, -- 처리 상태
    file_path VARCHAR2(255), -- 첨부 이미지
    created_at TIMESTAMP DEFAULT SYSDATE, -- 신청일
    process_date TIMESTAMP, -- 처리일
    refund_amount NUMBER, -- 환불 금액
    FOREIGN KEY (order_detail_id) REFERENCES order_detail(order_detail_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

-- 코멘트
COMMENT ON TABLE return_request IS '반품 요청';
COMMENT ON COLUMN return_request.return_id IS '반품 번호';
COMMENT ON COLUMN return_request.order_detail_id IS '주문 상세 번호';
COMMENT ON COLUMN return_request.member_id IS '회원 번호';
COMMENT ON COLUMN return_request.reason_type IS '반품 사유 구분';
COMMENT ON COLUMN return_request.reason IS '상세 사유';
COMMENT ON COLUMN return_request.status IS '처리 상태';
COMMENT ON COLUMN return_request.file_path IS '첨부 이미지';
COMMENT ON COLUMN return_request.created_at IS '신청일';
COMMENT ON COLUMN return_request.process_date IS '처리일';
COMMENT ON COLUMN return_request.refund_amount IS '환불 금액';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO return_request (return_id, order_detail_id, member_id, reason_type, reason, status, file_path, created_at, process_date, refund_amount) VALUES (return_seq.NEXTVAL, 1, 1, '테스트_reason_type_1', '사이즈가 안 맞아요', '요청됨', '테스트_file_path_1', SYSDATE, SYSDATE, 1);
INSERT INTO return_request (return_id, order_detail_id, member_id, reason_type, reason, status, file_path, created_at, process_date, refund_amount) VALUES (return_seq.NEXTVAL, 2, 2, '테스트_reason_type_2', '상품에 오염이 있습니다', '수거중', '테스트_file_path_2', SYSDATE, SYSDATE, 2);
INSERT INTO return_request (return_id, order_detail_id, member_id, reason_type, reason, status, file_path, created_at, process_date, refund_amount) VALUES (return_seq.NEXTVAL, 3, 3, '테스트_reason_type_3', '단순 변심', '환불완료', '테스트_file_path_3', SYSDATE, SYSDATE, 3);
COMMIT;
