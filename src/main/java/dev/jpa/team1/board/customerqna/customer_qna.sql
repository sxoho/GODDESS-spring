-- =========================================================
-- CUSTOMER_QNA
-- 고객 문의
-- =========================================================
--
-- 중요
-- 현재 팀 DB에는 CUSTOMER_QNA 테이블과 CUSTOMER_QNA_SEQ가 이미 존재하므로
-- 아래의 "현재 DB 정리/테스트" 구간부터 실행합니다.
--
-- 정식 PK 컬럼명은 customer_qna_id 입니다.
-- 잘못 추가된 customer_qna_id 컬럼은 제거합니다.
-- =========================================================


-- =========================================================
-- 0. 최초 DB 생성용
-- ※ 새 DB를 처음 구성할 때만 사용
-- ※ 현재 팀 DB에서는 실행하지 말 것
-- =========================================================

/*
CREATE SEQUENCE customer_qna_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;


CREATE TABLE customer_qna (

    customer_qna_id NUMBER
        CONSTRAINT pk_customer_qna PRIMARY KEY,

    member_id NUMBER NOT NULL,

    category VARCHAR2(50) NOT NULL,

    order_id NUMBER,

    title VARCHAR2(255) NOT NULL,

    content CLOB NOT NULL,

    answer CLOB,

    status VARCHAR2(50)
        DEFAULT 'WAITING'
        NOT NULL,

    created_at TIMESTAMP
        DEFAULT SYSTIMESTAMP
        NOT NULL,

    answer_date TIMESTAMP,

    CONSTRAINT fk_customer_qna_member
        FOREIGN KEY (member_id)
        REFERENCES member(member_id),

    CONSTRAINT fk_customer_qna_orders
        FOREIGN KEY (order_id)
        REFERENCES orders(order_id),

    CONSTRAINT ck_customer_qna_status
        CHECK (
            status IN (
                'WAITING',
                'ANSWERED'
            )
        )
);


COMMENT ON TABLE customer_qna
IS '고객 문의';

COMMENT ON COLUMN customer_qna.customer_qna_id
IS '고객 문의 번호';

COMMENT ON COLUMN customer_qna.member_id
IS '문의 작성 회원 번호';

COMMENT ON COLUMN customer_qna.category
IS '문의 유형';

COMMENT ON COLUMN customer_qna.order_id
IS '관련 주문 번호, 주문과 관계없는 문의는 NULL';

COMMENT ON COLUMN customer_qna.title
IS '문의 제목';

COMMENT ON COLUMN customer_qna.content
IS '문의 내용';

COMMENT ON COLUMN customer_qna.answer
IS '관리자 답변';

COMMENT ON COLUMN customer_qna.status
IS '처리 상태: WAITING 답변대기, ANSWERED 답변완료';

COMMENT ON COLUMN customer_qna.created_at
IS '문의 등록일';

COMMENT ON COLUMN customer_qna.answer_date
IS '관리자 답변일';


CREATE INDEX idx_customer_qna_member
ON customer_qna(member_id);

CREATE INDEX idx_customer_qna_order
ON customer_qna(order_id);

CREATE INDEX idx_customer_qna_status
ON customer_qna(status);

CREATE INDEX idx_customer_qna_created
ON customer_qna(created_at);
*/


-- =========================================================
-- 1. 현재 DB 컬럼 확인
-- =========================================================

SELECT
    column_id,
    column_name,
    data_type,
    nullable
FROM user_tab_columns
WHERE table_name = 'CUSTOMER_QNA'
ORDER BY column_id;


-- =========================================================
-- 2. 잘못 추가된 CUSTOMER_QNA_ID 컬럼 제거
--    정식 컬럼 customer_qna_id는 유지
--
--    컬럼이 존재할 때만 제거되므로 재실행해도 안전합니다.
-- =========================================================

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM user_tab_columns
    WHERE table_name = 'CUSTOMER_QNA'
      AND column_name = 'CUSTOMER_QNA_ID';

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE
            'ALTER TABLE customer_qna DROP COLUMN customer_qna_id';
    END IF;
END;
/


-- =========================================================
-- 3. 컬럼 다시 확인
--    customer_qna_id만 남아 있으면 정상
-- =========================================================

SELECT
    column_id,
    column_name,
    data_type,
    nullable
FROM user_tab_columns
WHERE table_name = 'CUSTOMER_QNA'
ORDER BY column_id;


-- =========================================================
-- 4. 기존 한글 상태값 정리
-- =========================================================

UPDATE customer_qna
SET status = 'ANSWERED'
WHERE status = '답변완료';


UPDATE customer_qna
SET status = 'WAITING'
WHERE status IN (
    '처리중',
    '답변대기'
);


COMMIT;


-- =========================================================
-- 5. 현재 고객 문의 조회
-- =========================================================

SELECT
    customer_qna_id,
    member_id,
    category,
    order_id,
    title,
    status,
    created_at,
    answer_date
FROM customer_qna
ORDER BY customer_qna_id DESC;


-- =========================================================
-- 6. 실제 회원 번호 확인
-- =========================================================

SELECT
    member_id,
    login_id,
    nickname
FROM member
ORDER BY member_id;


-- =========================================================
-- 7. 테스트 데이터 등록
--
-- 아래 예시는 member_id = 4 사용
-- 실제 MEMBER 테이블에 4번 회원이 있을 때만 실행
-- 주문과 관계없는 문의이므로 order_id는 NULL
-- =========================================================

INSERT INTO customer_qna (
    customer_qna_id,
    member_id,
    category,
    order_id,
    title,
    content,
    answer,
    status,
    created_at,
    answer_date
)
VALUES (
    customer_qna_seq.NEXTVAL,
    4,
    '기타문의',
    NULL,
    '고객 문의 테스트입니다.',
    '고객 문의 기능 테스트를 위해 작성한 문의입니다.',
    NULL,
    'WAITING',
    SYSTIMESTAMP,
    NULL
);


COMMIT;


-- =========================================================
-- 8. 테스트 등록 결과 확인
-- =========================================================

SELECT
    customer_qna_id,
    member_id,
    category,
    order_id,
    title,
    content,
    answer,
    status,
    created_at,
    answer_date
FROM customer_qna
ORDER BY customer_qna_id DESC;


-- =========================================================
-- 9. 답변 대기 문의 조회
-- 관리자 화면의 "답변대기"
-- =========================================================

SELECT
    customer_qna_id,
    member_id,
    category,
    order_id,
    title,
    status,
    created_at
FROM customer_qna
WHERE status = 'WAITING'
ORDER BY created_at DESC;


-- =========================================================
-- 10. 답변 완료 문의 조회
-- 관리자 화면의 "답변완료"
-- =========================================================

SELECT
    customer_qna_id,
    member_id,
    category,
    order_id,
    title,
    answer,
    status,
    created_at,
    answer_date
FROM customer_qna
WHERE status = 'ANSWERED'
ORDER BY created_at DESC;


-- =========================================================
-- 11. MEMBER JOIN
-- 관리자 고객문의 목록에서 작성자 확인
-- =========================================================

SELECT
    cq.customer_qna_id,
    cq.member_id,
    m.login_id,
    m.nickname,
    cq.category,
    cq.order_id,
    cq.title,
    cq.status,
    cq.created_at,
    cq.answer_date
FROM customer_qna cq
JOIN member m
    ON cq.member_id = m.member_id
ORDER BY cq.created_at DESC;


-- =========================================================
-- 12. 관리자 답변 처리 SQL 예시
-- 실제 프로그램에서는 Spring에서 처리
-- 수동 테스트 시 customer_qna_id를 실제 값으로 수정
-- =========================================================

/*
UPDATE customer_qna
SET
    answer = '안녕하세요. 문의해주신 내용을 확인하여 답변드립니다.',
    status = 'ANSWERED',
    answer_date = SYSTIMESTAMP
WHERE customer_qna_id = 1;

COMMIT;
*/


-- =========================================================
-- 13. 답변 초기화 테스트
-- =========================================================

/*
UPDATE customer_qna
SET
    answer = NULL,
    status = 'WAITING',
    answer_date = NULL
WHERE customer_qna_id = 1;

COMMIT;
*/


-- =========================================================
-- 14. 테스트 데이터 삭제
-- =========================================================

/*
DELETE FROM customer_qna
WHERE customer_qna_id = 1;

COMMIT;
*/
