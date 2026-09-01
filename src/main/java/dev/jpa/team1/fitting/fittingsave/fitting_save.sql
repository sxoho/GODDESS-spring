-- FittingSave 테이블 및 시퀀스 생성

CREATE SEQUENCE fitting_save_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE fitting_save (
    fitting_save_id NUMBER PRIMARY KEY, -- 저장 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    fitting_type VARCHAR2(50) NOT NULL, -- 피팅 종류(간편 피팅/코디 피팅)
    quick_fitting_result_id NUMBER, -- 간편 피팅 결과 번호 (외래키, quick_fitting_result 테이블 참조)
    coord_fitting_result_id NUMBER, -- 코디 피팅 결과 번호 (외래키, coord_fitting_result 테이블 참조)
    save_name VARCHAR2(100), -- 저장 이름
    memo CLOB, -- 메모
    created_at TIMESTAMP DEFAULT SYSDATE, -- 저장일
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (quick_fitting_result_id) REFERENCES quick_fitting_result(quick_fitting_result_id),
    FOREIGN KEY (coord_fitting_result_id) REFERENCES coord_fitting_result(coord_fitting_result_id)
);

-- 코멘트
COMMENT ON TABLE fitting_save IS '저장한 피팅';
COMMENT ON COLUMN fitting_save.fitting_save_id IS '저장 번호';
COMMENT ON COLUMN fitting_save.member_id IS '회원 번호';
COMMENT ON COLUMN fitting_save.fitting_type IS '피팅 종류(간편 피팅/코디 피팅)';
COMMENT ON COLUMN fitting_save.quick_fitting_result_id IS '간편 피팅 결과 번호';
COMMENT ON COLUMN fitting_save.coord_fitting_result_id IS '코디 피팅 결과 번호';
COMMENT ON COLUMN fitting_save.save_name IS '저장 이름';
COMMENT ON COLUMN fitting_save.memo IS '메모';
COMMENT ON COLUMN fitting_save.created_at IS '저장일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO fitting_save (fitting_save_id, member_id, fitting_type, quick_fitting_result_id, coord_fitting_result_id, save_name, memo, created_at) VALUES (fitting_save_seq.NEXTVAL, 1, '테스트_fitting_type_1', 1, 1, '테스트_save_name_1', '테스트_memo_1', SYSDATE);
INSERT INTO fitting_save (fitting_save_id, member_id, fitting_type, quick_fitting_result_id, coord_fitting_result_id, save_name, memo, created_at) VALUES (fitting_save_seq.NEXTVAL, 2, '테스트_fitting_type_2', 2, 2, '테스트_save_name_2', '테스트_memo_2', SYSDATE);
INSERT INTO fitting_save (fitting_save_id, member_id, fitting_type, quick_fitting_result_id, coord_fitting_result_id, save_name, memo, created_at) VALUES (fitting_save_seq.NEXTVAL, 3, '테스트_fitting_type_3', 3, 3, '테스트_save_name_3', '테스트_memo_3', SYSDATE);
COMMIT;
