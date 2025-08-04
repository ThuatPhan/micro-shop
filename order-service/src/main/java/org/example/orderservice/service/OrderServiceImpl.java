package org.example.orderservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.example.orderservice.dto.request.OrderItemRequest;
import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.request.ProductBatchRequest;
import org.example.orderservice.dto.response.ApiResponse;
import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.mapper.OrderItemMapper;
import org.example.orderservice.repository.OrderItemRepository;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.repository.client.ProductServiceClient;
import org.example.orderservice.util.SecurityUtils;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    ProductServiceClient productServiceClient;
    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;
    OrderItemMapper orderItemMapper;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Map<String, ProductResponse> productMap = fetchProductData(request);
        Order order = saveOrderWithTotal(request, productMap);
        List<OrderItem> savedItems = saveOrderItems(request, order);
        return mapToOrderResponse(savedItems, productMap);
    }

    private Order saveOrderWithTotal(OrderRequest request, Map<String, ProductResponse> productMap) {
        double totalAmount = request.getItems().stream()
                .mapToDouble(item -> {
                    ProductResponse product = productMap.get(item.getProduct());
                    return product.getPrice() * item.getQuantity();
                })
                .sum();

        Order order = Order.builder()
                .userId(SecurityUtils.extractUserId())
                .totalAmount(totalAmount)
                .build();

        return orderRepository.save(order);
    }

    private List<OrderItem> saveOrderItems(OrderRequest request, Order order) {
        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> {
                    OrderItem item = orderItemMapper.toOrderItem(itemRequest);
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toList());

        return orderItemRepository.saveAll(items);
    }

    private OrderResponse mapToOrderResponse(List<OrderItem> orderItems, Map<String, ProductResponse> productMap) {
        List<OrderItemResponse> responses = orderItemMapper.toOrderItemResponseList(orderItems, productMap);
        return OrderResponse.builder().items(responses).build();
    }

    private Map<String, ProductResponse> fetchProductData(OrderRequest request) {
        List<String> productIds =
                request.getItems().stream().map(OrderItemRequest::getProduct).collect(Collectors.toList());

        if (productIds.isEmpty()) return Collections.emptyMap();

        ApiResponse<List<ProductResponse>> response = productServiceClient.getProducts(
                ProductBatchRequest.builder().productIds(productIds).build());

        return response.getData().stream().collect(Collectors.toMap(ProductResponse::getId, Function.identity()));
    }
}
