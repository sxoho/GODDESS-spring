-- RecommendProduct 테이블 및 시퀀스 생성

CREATE SEQUENCE recommend_product_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE recommend_product (
    recommend_product_id NUMBER PRIMARY KEY, -- 추천 상품 번호 (기본키)
    recommendation_id NUMBER NOT NULL, -- 스타일 추천 번호 (외래키, style_recommendation 테이블 참조)
    product_color_id NUMBER NOT NULL, -- 상품 색상 번호 (외래키, product_color 테이블 참조)
    item_type VARCHAR2(50) NOT NULL, -- 상품 역할
    sort_order NUMBER NOT NULL, -- 추천 순서
    reason VARCHAR2(255), -- 추천 이유
    FOREIGN KEY (recommendation_id) REFERENCES style_recommendation(recommendation_id),
    FOREIGN KEY (product_color_id) REFERENCES product_color(product_color_id)
);

-- 코멘트
COMMENT ON TABLE recommend_product IS '추천 상품';
COMMENT ON COLUMN recommend_product.recommend_product_id IS '추천 상품 번호';
COMMENT ON COLUMN recommend_product.recommendation_id IS '스타일 추천 번호';
COMMENT ON COLUMN recommend_product.product_color_id IS '상품 색상 번호';
COMMENT ON COLUMN recommend_product.item_type IS '상품 역할';
COMMENT ON COLUMN recommend_product.sort_order IS '추천 순서';
COMMENT ON COLUMN recommend_product.reason IS '추천 이유';

-- 예시 데이터 등록 (Mock Data)
INSERT INTO recommend_product (recommend_product_id, recommendation_id, product_color_id, item_type, sort_order, reason) VALUES (recommend_product_seq.NEXTVAL, 1, 1, '테스트_item_type_1', 1, '현재 트렌드와 고객님의 선호 스타일 일치');
INSERT INTO recommend_product (recommend_product_id, recommendation_id, product_color_id, item_type, sort_order, reason) VALUES (recommend_product_seq.NEXTVAL, 2, 2, '테스트_item_type_2', 2, '이전에 구매하신 상품과 어울리는 코디 아이템');
INSERT INTO recommend_product (recommend_product_id, recommendation_id, product_color_id, item_type, sort_order, reason) VALUES (recommend_product_seq.NEXTVAL, 3, 3, '테스트_item_type_3', 3, '가을 시즌 베스트셀러');
COMMIT;
