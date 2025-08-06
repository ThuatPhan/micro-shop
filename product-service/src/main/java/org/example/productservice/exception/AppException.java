package org.example.productservice.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppException extends RuntimeException {
    ErrorCode errorCode;
    String customMessage;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.customMessage = null;
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.customMessage = customMessage;
        this.errorCode = errorCode;
    }
}
