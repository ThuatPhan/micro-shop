package org.example.cartservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.request.ProductBatchRequest;
import org.example.cartservice.dto.response.ApiResponse;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.PageResponse;
import org.example.cartservice.dto.response.ProductResponse;
import org.example.cartservice.entity.Cart;
import org.example.cartservice.entity.CartItem;
import org.example.cartservice.exception.AppException;
import org.example.cartservice.exception.ErrorCode;
import org.example.cartservice.mapper.CartItemMapper;
import org.example.cartservice.repository.CartItemRepository;
import org.example.cartservice.repository.CartRepository;
import org.example.cartservice.repository.client.ProductServiceClient;
import org.example.cartservice.util.SecurityUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    ProductServiceClient productServiceClient;
    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    CartItemMapper cartItemMapper;

    @Transactional
    @Override
    public CartItemResponse addCartItem(CartItemRequest request) {
        String userId = SecurityUtils.extractUserId();

        // spotless:off
        Cart cart = cartRepository
                .findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).build()));
        // spotless:on

        // Call Product Service to validate and fetch product info
        ApiResponse<ProductResponse> response = productServiceClient.getProduct(request.getProduct());
        ProductResponse product = response.getData();

        Optional<CartItem> existingCartItemOpt =
                cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        CartItem cartItem;

        if (existingCartItemOpt.isPresent()) {
            // If the item already exists in the cart, update its quantity
            cartItem = existingCartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            // If the item is new, create a new CartItem
            cartItem = cartItemMapper.toCartItem(request);
            cartItem.setCart(cart);
            cartItem.setProductId(product.getId());
        }

        CartItem savedItem = cartItemRepository.save(cartItem);

        return cartItemMapper.toCartItemResponse(savedItem, product);
    }

    @Override
    public PageResponse<CartItemResponse> getCartItems(int page, int size) {
        String userId = SecurityUtils.extractUserId();

        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null) {
            return PageResponse.of(false, Collections.emptyList());
        }

        var cartItemsPage = cartItemRepository.findByCart(cart, PageRequest.of(page, size));
        List<CartItem> cartItems = cartItemsPage.getContent();

        // Resolve productId → ProductResponse
        Map<String, ProductResponse> productMap =
                resolveProducts(cartItems.stream().map(CartItem::getProductId).toList());

        // Map CartItem + ProductResponse → CartItemResponse
        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(item -> cartItemMapper.toCartItemResponse(item, productMap.get(item.getProductId())))
                .toList();

        return PageResponse.of(cartItemsPage.hasNext(), itemResponses);
    }

    @Transactional
    @Override
    public CartItemResponse updateCartItem(String itemId, Long quantity) {
        String userId = SecurityUtils.extractUserId();

        CartItem cartItem =
                cartItemRepository.findById(itemId).orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!cartItem.getCart().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (quantity <= 0) {
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }

        cartItem.setQuantity(quantity);
        CartItem updated = cartItemRepository.save(cartItem);

        ProductResponse product =
                productServiceClient.getProduct(cartItem.getProductId()).getData();

        return cartItemMapper.toCartItemResponse(updated, product);
    }

    @Transactional
    @Override
    public void deleteCartItem(String itemId) {
        String userId = SecurityUtils.extractUserId();

        CartItem cartItem =
                cartItemRepository.findById(itemId).orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!cartItem.getCart().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        cartItemRepository.delete(cartItem);
    }

    private Map<String, ProductResponse> resolveProducts(List<String> productIds) {
        if (productIds.isEmpty()) return Collections.emptyMap();

        ApiResponse<List<ProductResponse>> response = productServiceClient.getProducts(
                ProductBatchRequest.builder().productIds(productIds).build());

        return response.getData().stream().collect(Collectors.toMap(ProductResponse::getId, Function.identity()));
    }
}
