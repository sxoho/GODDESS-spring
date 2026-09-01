-- 기존 테이블 및 시퀀스 삭제
DROP TABLE product_qna CASCADE CONSTRAINTS;
DROP SEQUENCE product_qna_seq;

-- 시퀀스 생성
CREATE SEQUENCE product_qna_seq START WITH 1 INCREMENT BY 1;

-- 테이블 생성
CREATE TABLE product_qna (
    product_qna_id NUMBER PRIMARY KEY,
    product_id NUMBER NOT NULL,
    member_id NUMBER NOT NULL,
    title VARCHAR2(255) NOT NULL,
    content CLOB NOT NULL,
    secret VARCHAR2(1) DEFAULT 'N' NOT NULL,
    answer CLOB,
    answer_id NUMBER,
    status VARCHAR2(50) DEFAULT 'WAITING' NOT NULL,
    created_at TIMESTAMP DEFAULT SYSDATE NOT NULL,
    answer_date TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (answer_id) REFERENCES member(member_id),
    CHECK (secret IN ('Y', 'N')),
    CHECK (status IN ('WAITING', 'ANSWERED', 'CLOSED'))
);

-- 코멘트
COMMENT ON TABLE product_qna IS '상품 문의';
COMMENT ON COLUMN product_qna.product_qna_id IS '상품 문의 번호';
COMMENT ON COLUMN product_qna.product_id IS '상품 번호';
COMMENT ON COLUMN product_qna.member_id IS '회원 번호';
COMMENT ON COLUMN product_qna.title IS '제목';
COMMENT ON COLUMN product_qna.content IS '내용';
COMMENT ON COLUMN product_qna.secret IS '비밀글 여부(Y/N)';
COMMENT ON COLUMN product_qna.answer IS '답변 내용';
COMMENT ON COLUMN product_qna.answer_id IS '답변 관리자 번호';
COMMENT ON COLUMN product_qna.status IS '처리 상태';
COMMENT ON COLUMN product_qna.created_at IS '등록일';
COMMENT ON COLUMN product_qna.answer_date IS '답변일';

-- 예시 데이터
INSERT INTO product_qna (product_qna_id, product_id, member_id, title, content, secret, answer, answer_id, status, created_at, answer_date)
VALUES (product_qna_seq.NEXTVAL, 1, 1, '사이즈 추천 부탁드립니다', '키 175에 몸무게 70인데 무슨 사이즈가 맞을까요?', 'N', 'L 사이즈를 추천드립니다. 상세 실측 사이즈를 참고해주세요.', 1, 'ANSWERED', SYSDATE, SYSDATE);

INSERT INTO product_qna (product_qna_id, product_id, member_id, title, content, secret, answer, answer_id, status, created_at, answer_date)
VALUES (product_qna_seq.NEXTVAL, 2, 2, '세탁은 어떻게 하나요?', '울 소재인데 물세탁해도 되나요?', 'Y', '드라이클리닝을 권장합니다. 물세탁 시 수축이 발생할 수 있습니다.', 2, 'ANSWERED', SYSDATE, SYSDATE);

INSERT INTO product_qna (product_qna_id, product_id, member_id, title, content, secret, answer, answer_id, status, created_at, answer_date)
VALUES (product_qna_seq.NEXTVAL, 3, 3, '남녀 공용인가요?', '여자가 입기에는 많이 클까요?', 'N', NULL, NULL, 'WAITING', SYSDATE, NULL);

COMMIT;


select * from PRODUCT_QNA;