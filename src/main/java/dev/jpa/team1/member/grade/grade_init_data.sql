-- Grade 초기 데이터 등록
--
-- 등급 기준
-- 1 ~ 3  : ADMIN
-- 4 ~ 6  : USER
-- 7 ~ 9  : GOLD
-- 10     : VIP

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    1,
    'ADMIN_1',
    '관리자 1등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    2,
    'ADMIN_2',
    '관리자 2등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    3,
    'ADMIN_3',
    '관리자 3등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    4,
    'USER_1',
    '일반 회원 1등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    5,
    'USER_2',
    '일반 회원 2등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    6,
    'USER_3',
    '일반 회원 3등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    7,
    'GOLD_1',
    'GOLD 회원 1등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    8,
    'GOLD_2',
    'GOLD 회원 2등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    9,
    'GOLD_3',
    'GOLD 회원 3등급',
    'Y',
    SYSDATE
);

INSERT INTO grade (
    grade_id,
    name,
    description,
    visible,
    created_at
)
VALUES (
    10,
    'VIP',
    'VIP 회원',
    'Y',
    SYSDATE
);

COMMIT;