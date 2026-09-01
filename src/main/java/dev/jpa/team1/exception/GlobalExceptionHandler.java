package dev.jpa.team1.exception;

import dev.jpa.team1.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 커스텀 에러 처리 수정
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        // 커스텀 메시지가 존재하면 우선 사용, 없으면 ErrorCode의 기본 메시지 사용
        String message = ex.getCustomMessage() != null ? ex.getCustomMessage() : errorCode.getMessage();

        log.warn("CustomException occurred: Code={}, Message={}", errorCode.getCode(), message);

        return ResponseEntity
                .status(errorCode.getStatus())
                // ApiResponse.error(errorCode, customMessage) 오버로딩 메서드 호출
                .body(ApiResponse.error(errorCode, message));
    }

    // DTO @Valid 유효성 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();
        
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[")
                   .append(fieldError.getField())
                   .append("](은)는 ")
                   .append(fieldError.getDefaultMessage())
                   .append(" ");
        }
        
        log.warn("Validation error: {}", builder.toString());

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE, builder.toString().trim()));
    }

    // 그 외 서버 내부 예상치 못한 에러 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        log.error("Internal Server Error occurred", ex);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}