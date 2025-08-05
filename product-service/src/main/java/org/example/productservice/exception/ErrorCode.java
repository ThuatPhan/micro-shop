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
    UNCATEGORIZED(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error"),
    UNAUTHENTICATED(1000, HttpStatus.UNAUTHORIZED, "Unauthenticated request"),
    ACCESS_DENIED(1001, HttpStatus.FORBIDDEN, "Access denied"),

    CATEGORY_ALREADY_EXISTS(2000, HttpStatus.BAD_REQUEST, "Category already exists"),
    CATEGORY_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "Category not found"),
    PRODUCT_ALREADY_EXISTS(2002, HttpStatus.BAD_REQUEST, "Products already exists"),
    PRODUCT_NOT_FOUND(2003, HttpStatus.NOT_FOUND, "Product not found"),
    ;

    int code;
    HttpStatus statusCode;
    String message;
}
