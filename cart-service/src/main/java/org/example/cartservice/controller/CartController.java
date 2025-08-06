package org.example.cartservice.controller;

import jakarta.validation.Valid;

import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.response.ApiResponse;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.PageResponse;
import org.example.cartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Tag(name = "Cart API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CartItemResponse> addCartItem(@RequestBody @Valid CartItemRequest request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), cartService.addCartItem(request));
    }

    @GetMapping
    public ApiResponse<PageResponse<CartItemResponse>> getCartItems(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(HttpStatus.OK.value(), cartService.getCartItems(page, size));
    }

    @PutMapping("/{itemId}")
    public ApiResponse<CartItemResponse> updateCartItem(@PathVariable String itemId, @RequestParam Long quantity) {
        return ApiResponse.success(HttpStatus.OK.value(), cartService.updateCartItem(itemId, quantity));
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable String itemId) {
        cartService.deleteCartItem(itemId);
    }
}
