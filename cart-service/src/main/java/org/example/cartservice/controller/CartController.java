package org.example.cartservice.controller;

import jakarta.validation.Valid;

import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.response.ApiResponse;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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
