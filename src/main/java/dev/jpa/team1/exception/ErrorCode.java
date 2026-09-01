package dev.jpa.team1.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // --- Common ---
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),

    // --- Member & Auth ---
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "회원 정보를 찾을 수 없습니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "M002", "이미 존재하는 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "M003", "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "M004", "비밀번호가 일치하지 않습니다."),
    GRADE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "M005", "회원 등급 정보를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "M006", "유효하지 않거나 만료된 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "M007", "존재하지 않는 리프레시 토큰입니다. 다시 로그인해주세요."),

    // --- Product ---
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품 정보를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "P002", "카테고리 정보를 찾을 수 없습니다."),

    // --- Fitting ---
    FITTING_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "피팅 요청 정보를 찾을 수 없습니다."),
    FITTING_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "F002", "피팅 결과 정보를 찾을 수 없습니다."),
    UNAUTHORIZED_FITTING_ACCESS(HttpStatus.FORBIDDEN, "F003", "해당 피팅 정보에 대한 접근 권한이 없습니다."),
    INVALID_FITTING_TYPE(HttpStatus.BAD_REQUEST, "F004", "지원하지 않는 피팅 종류입니다."),

    // --- Commerce & Order ---
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "O001", "장바구니에서 해당 상품을 찾을 수 없습니다."),
    PRODUCT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "O002", "선택한 상품 옵션 정보를 찾을 수 없습니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "O003", "상품의 재고가 부족합니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O004", "주문 정보를 찾을 수 없습니다."),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "O005", "해당 주문의 배송 정보를 찾을 수 없습니다."),
    ORDER_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "O006", "주문 상세(상품) 정보를 찾을 수 없습니다."),
    RETURN_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "O007", "반품 요청 정보를 찾을 수 없습니다."),
    INVALID_RETURN_STATE(HttpStatus.BAD_REQUEST, "O008", "반품을 신청할 수 없거나 이미 처리된 상태입니다."),

    // --- Chat ---
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CH001", "채팅방 정보를 찾을 수 없습니다."),
    INVALID_CHAT_ACCESS(HttpStatus.FORBIDDEN, "CH002", "해당 채팅방에 접근할 권한이 없습니다."),
    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "CH003", "FAQ 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}