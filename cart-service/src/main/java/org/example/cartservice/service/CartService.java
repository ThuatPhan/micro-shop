package org.example.cartservice.service;

import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.PageResponse;

public interface CartService {
    CartItemResponse addCartItem(CartItemRequest request);

    PageResponse<CartItemResponse> getCartItems(int page, int size);

    CartItemResponse updateCartItem(String itemId, Long quantity);

    void deleteCartItem(String itemId);
}
