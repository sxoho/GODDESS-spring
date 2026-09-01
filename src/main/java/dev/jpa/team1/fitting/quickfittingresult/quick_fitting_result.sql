-- QuickFittingResult 테이블 및 시퀀스 생성

CREATE SEQUENCE quick_fitting_result_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE quick_fitting_result (
    quick_fitting_result_id NUMBER PRIMARY KEY, -- 간편 피팅 결과 번호 (기본키)
    quick_fitting_id NUMBER NOT NULL, -- 간편 피팅 번호 (외래키, quick_fitting 테이블 참조)
    product_color_id NUMBER NOT NULL, -- 상품 색상 번호 (외래키, product_color 테이블 참조)
    file_origin VARCHAR2(255), -- 결과 이미지 원본명
    file_saved VARCHAR2(255), -- 결과 이미지 저장명
    file_path VARCHAR2(500), -- 결과 이미지 경로
    ai_description CLOB, -- AI 분위기 설명
    created_at TIMESTAMP DEFAULT SYSDATE, -- 생성일
    success VARCHAR2(1) NOT NULL, -- 생성 성공 여부(Y/N)
    FOREIGN KEY (quick_fitting_id) REFERENCES quick_fitting(quick_fitting_id),
    FOREIGN KEY (product_color_id) REFERENCES product_color(product_color_id)
);

-- 코멘트
COMMENT ON TABLE quick_fitting_result IS '간편 피팅 결과';
COMMENT ON COLUMN quick_fitting_result.quick_fitting_result_id IS '간편 피팅 결과 번호';
COMMENT ON COLUMN quick_fitting_result.quick_fitting_id IS '간편 피팅 번호';
COMMENT ON COLUMN quick_fitting_result.product_color_id IS '상품 색상 번호';
COMMENT ON COLUMN quick_fitting_result.file_origin IS '결과 이미지 원본명';
COMMENT ON COLUMN quick_fitting_result.file_saved IS '결과 이미지 저장명';
COMMENT ON COLUMN quick_fitting_result.file_path IS '결과 이미지 경로';
COMMENT ON COLUMN quick_fitting_result.ai_description IS 'AI 분위기 설명';
COMMENT ON COLUMN quick_fitting_result.created_at IS '생성일';
COMMENT ON COLUMN quick_fitting_result.success IS '생성 성공 여부(Y/N)';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO quick_fitting_result (quick_fitting_result_id, quick_fitting_id, product_color_id, file_origin, file_saved, file_path, ai_description, created_at, success) VALUES (quick_fitting_result_seq.NEXTVAL, 1, 1, '테스트_file_origin_1', '테스트_file_saved_1', '테스트_file_path_1', '테스트_ai_description_1', SYSDATE, '테스트_success_1');
INSERT INTO quick_fitting_result (quick_fitting_result_id, quick_fitting_id, product_color_id, file_origin, file_saved, file_path, ai_description, created_at, success) VALUES (quick_fitting_result_seq.NEXTVAL, 2, 2, '테스트_file_origin_2', '테스트_file_saved_2', '테스트_file_path_2', '테스트_ai_description_2', SYSDATE, '테스트_success_2');
INSERT INTO quick_fitting_result (quick_fitting_result_id, quick_fitting_id, product_color_id, file_origin, file_saved, file_path, ai_description, created_at, success) VALUES (quick_fitting_result_seq.NEXTVAL, 3, 3, '테스트_file_origin_3', '테스트_file_saved_3', '테스트_file_path_3', '테스트_ai_description_3', SYSDATE, '테스트_success_3');
COMMIT;


-- 퀵 피팅 결과 테이블에 3D 모델 경로 컬럼 추가
ALTER TABLE quick_fitting_result ADD (model_file_path VARCHAR2(1000));
COMMENT ON COLUMN quick_fitting_result.model_file_path IS '3D 아바타 피팅 모델 파일 경로 (.glb)';
