package org.example.orderservice.exception;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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

    CART_NOT_FOUND(3000, HttpStatus.NOT_FOUND, "Cart not found"),
    CART_ITEM_NOT_FOUND(3001, HttpStatus.NOT_FOUND, "Cart item not found"),
    INVALID_QUANTITY(3002, HttpStatus.BAD_REQUEST, "Quantity must be greater than 0"),
    ;

    int code;
    HttpStatus statusCode;
    String message;

    private static final Map<Integer, ErrorCode> CODE_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(ErrorCode::getCode, e -> e));

    public static ErrorCode findByCode(int code) {
        return CODE_MAP.getOrDefault(code, UNCATEGORIZED);
    }
}
