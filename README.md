# GODDESS Spring Backend

여신강림 AI 뷰티 쇼핑/가상 체험 플랫폼 Spring Boot 백엔드입니다.

## 실행 기준

- Java 21
- Spring Boot 3.5.x
- Oracle SID: `XE`
- DB 계정: `GODDESS`
- Spring 포트: `9102` (기존 STAR/ONNIVER 프론트 연동을 최대한 유지)
- JPA: `spring.jpa.hibernate.ddl-auto=none`
- DB 생성 SQL: `db/GODDESS_ALL_WITH_MEMBERS.sql`

## 기존 기능 유지

회원/등급/로그인/인증/공지/문의/리뷰/찜/장바구니/주문/결제/배송 등 공용 기능은 가능한 한 기존 구조를 유지했습니다.

회원 등급은 기존 값 그대로 사용합니다.

- 1: USER
- 2: GOLD
- 3: VIP
- 4: ADMIN

기존 STAR 회원 11명의 `MEMBER_ID`와 데이터도 DB SQL에 포함되어 있으며, 신규 회원 ID 충돌 방지를 위해 `MEMBER_SEQ`는 1000부터 시작합니다.

## 뷰티 프로젝트 변경

- `PRODUCT_SIZE` -> `PRODUCT_VARIANT`
- `PRODUCT_COLOR_INFO` -> `PRODUCT_COLOR_MASTER`
- 의류 피팅 테이블 제거
- `BEAUTY_TRYON`, `BEAUTY_TRYON_ITEM`, `BEAUTY_TRYON_RESULT`, `BEAUTY_SAVE` 추가
- `SKIN_DIAGNOSIS_RESULT`, `PERSONAL_COLOR_RESULT` 추가
- `STYLE_RECOMMENDATION` -> `BEAUTY_RECOMMENDATION`
- 상품은 `usageInfo`, `productType`, `volume`, `skinType`, `personalColor`, `aiApplyEnabled` 기준으로 변경

## 실행 순서

1. GODDESS Oracle 계정에서 `db/GODDESS_ALL_WITH_MEMBERS.sql` 전체 실행
2. `src/main/resources/application.properties`의 DB 접속정보 확인
3. VSCode/Eclipse/IntelliJ에서 Gradle 프로젝트로 열기
4. `GoddessBackendApplication` 실행

> 업로드된 원본 프로젝트의 Gradle wrapper JAR가 빠져 있어, IDE의 Gradle 또는 로컬 Gradle을 사용해 실행할 수 있습니다.
