-- CoordFitting 테이블 및 시퀀스 생성

CREATE SEQUENCE coord_fitting_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE coord_fitting (
    coord_fitting_id NUMBER PRIMARY KEY, -- 코디 피팅 번호 (기본키)
    member_id NUMBER NOT NULL, -- 회원 번호 (외래키, member 테이블 참조)
    member_photo_id NUMBER, -- 회원 사진 번호 (외래키, member_photo 테이블 참조)
    fitting_profile_id NUMBER, -- 피팅 프로필 번호 (외래키, fitting_profile 테이블 참조)
    fitting_method VARCHAR2(20) NOT NULL, -- 피팅 방식(PHOTO/MODEL)
    top_color_id NUMBER, -- 선택한 상의 색상 번호
    bottom_color_id NUMBER, -- 선택한 하의 색상 번호
    outer_color_id NUMBER, -- 선택한 아우터 색상 번호
    status VARCHAR2(20) NOT NULL, -- 처리 상태
    error_message CLOB, -- 오류 메시지
    created_at TIMESTAMP DEFAULT SYSDATE, -- 생성일
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (member_photo_id) REFERENCES member_photo(member_photo_id),
    FOREIGN KEY (fitting_profile_id) REFERENCES fitting_profile(fitting_profile_id)
);

-- 코멘트
COMMENT ON TABLE coord_fitting IS '코디 피팅 요청';
COMMENT ON COLUMN coord_fitting.coord_fitting_id IS '코디 피팅 번호';
COMMENT ON COLUMN coord_fitting.member_id IS '회원 번호';
COMMENT ON COLUMN coord_fitting.member_photo_id IS '회원 사진 번호';
COMMENT ON COLUMN coord_fitting.fitting_profile_id IS '피팅 프로필 번호';
COMMENT ON COLUMN coord_fitting.fitting_method IS '피팅 방식(PHOTO/MODEL)';
COMMENT ON COLUMN coord_fitting.top_color_id IS '선택한 상의 색상 번호';
COMMENT ON COLUMN coord_fitting.bottom_color_id IS '선택한 하의 색상 번호';
COMMENT ON COLUMN coord_fitting.outer_color_id IS '선택한 아우터 색상 번호';
COMMENT ON COLUMN coord_fitting.status IS '처리 상태';
COMMENT ON COLUMN coord_fitting.error_message IS '오류 메시지';
COMMENT ON COLUMN coord_fitting.created_at IS '생성일';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO coord_fitting (coord_fitting_id, member_id, member_photo_id, fitting_profile_id, fitting_method, top_color_id, bottom_color_id, outer_color_id, status, error_message, created_at) VALUES (coord_fitting_seq.NEXTVAL, 1, 1, 1, '테스트_fitting_method_1', 1, 1, 1, '테스트_status_1', '테스트_error_message_1', SYSDATE);
INSERT INTO coord_fitting (coord_fitting_id, member_id, member_photo_id, fitting_profile_id, fitting_method, top_color_id, bottom_color_id, outer_color_id, status, error_message, created_at) VALUES (coord_fitting_seq.NEXTVAL, 2, 2, 2, '테스트_fitting_method_2', 2, 2, 2, '테스트_status_2', '테스트_error_message_2', SYSDATE);
INSERT INTO coord_fitting (coord_fitting_id, member_id, member_photo_id, fitting_profile_id, fitting_method, top_color_id, bottom_color_id, outer_color_id, status, error_message, created_at) VALUES (coord_fitting_seq.NEXTVAL, 3, 3, 3, '테스트_fitting_method_3', 3, 3, 3, '테스트_status_3', '테스트_error_message_3', SYSDATE);
COMMIT;
