package org.example.productservice.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    CATEGORY_ALREADY_EXISTS(1001, HttpStatus.BAD_REQUEST, "Category already exists"),
    CATEGORY_NOT_FOUND(1002, HttpStatus.NOT_FOUND, "Category not found"),
    PRODUCT_ALREADY_EXISTS(1003, HttpStatus.BAD_REQUEST, "Products already exists"),
    PRODUCT_NOT_FOUND(1004, HttpStatus.NOT_FOUND, "Product not found"),

    UNAUTHENTICATED(1005, HttpStatus.UNAUTHORIZED, "Unauthenticated request"),
    ACCESS_DENIED(1006, HttpStatus.FORBIDDEN, "Access denied");

    int code;
    HttpStatus statusCode;
    String message;
}
