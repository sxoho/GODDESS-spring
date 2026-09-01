package dev.jpa.team1.common;

import dev.jpa.team1.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;

    // 1. 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    // 2. 성공 응답 (데이터 없음 / 메세지 커스텀 가능)
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, "SUCCESS", message, null);
    }

    // 3. 실패 응답 (ErrorCode 기반)
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }

    // 4. 실패 응답 (메시지 오버라이딩 필요 시 - 예: Validation 에러 메시지)
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String customMessage) {
        return new ApiResponse<>(false, errorCode.getCode(), customMessage, null);
    }
}