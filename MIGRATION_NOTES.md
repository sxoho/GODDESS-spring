# GODDESS Spring 정리본 v2

- `dev.jpa.team1.fitting` 패키지 전체 삭제
- `member.fittingprofile` 삭제
- 의류 전용 `productsize`, `productcolorinfo`, `stylerecommendation` 삭제
- 기존 피팅 ErrorCode를 BEAUTY_TRYON 계열로 변경
- 채팅 메시지 유형의 `FITTING_CARD`를 `BEAUTY_CARD`로 변경
- 예전 의류 스키마를 담고 있던 패키지 내부 SQL 조각 삭제
- DB 기준 파일: `db/GODDESS_ALL_WITH_MEMBERS.sql`
- build/bin/.gradle/.git 등 생성물 삭제
