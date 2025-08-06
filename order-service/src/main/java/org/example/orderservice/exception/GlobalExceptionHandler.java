package org.example.orderservice.exception;

import org.example.orderservice.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> appExceptionHandler(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        log.error(
                "Handled AppException - code: {}, message: {}", errorCode.getCode(), errorCode.getMessage(), exception);

        return ResponseEntity.badRequest().body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException exception) {

        String field =
                exception.getFieldError() != null ? exception.getFieldError().getField() : "unknown";
        String message =
                exception.getFieldError() != null ? exception.getFieldError().getDefaultMessage() : "Validation error";

        log.warn("Validation failed - field: {}, message: {}", field, message);

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> accessDeniedExceptionHandler(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;

        log.warn("Access denied: {}", exception.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> runtimeExceptionHandler(RuntimeException exception) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;

        log.error("Unhandled RuntimeException: {}", exception.getMessage(), exception);

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }
}
