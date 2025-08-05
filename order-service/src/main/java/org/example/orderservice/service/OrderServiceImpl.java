package org.example.orderservice.service;

import static org.example.orderservice.constant.RabbitMQConstant.EXCHANGE;
import static org.example.orderservice.constant.RabbitMQConstant.ROUTING_KEY;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.example.orderservice.dto.request.OrderItemRequest;
import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.request.ProductBatchRequest;
import org.example.orderservice.dto.response.*;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.exception.AppException;
import org.example.orderservice.exception.ErrorCode;
import org.example.orderservice.mapper.OrderItemMapper;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderItemRepository;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.repository.client.ProductServiceClient;
import org.example.orderservice.util.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    ProductServiceClient productServiceClient;
    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;
    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        var productIds = request.getItems()
                .stream()
                .map(OrderItemRequest::getProduct)
                .collect(Collectors.toList());

        Map<String, ProductResponse> productMap = fetchProductData(productIds);

        Order order = saveOrderWithTotal(request, productMap);

        List<OrderItem> savedItems = saveOrderItems(request, order);

        OrderResponse orderResponse = mapToOrderResponse(order, savedItems, productMap);

        try {
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, objectMapper.writeValueAsString(orderResponse));
        } catch (JsonProcessingException e) {
            log.error("Error converting order response to JSON for RabbitMQ: {}", e.getMessage());
        }

        return orderResponse;
    }

    @Override
    public OrderResponse getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        List<String> productIds = items.stream().map(OrderItem::getProductId).collect(Collectors.toList());
        Map<String, ProductResponse> productMap = fetchProductData(productIds);

        return mapToOrderResponse(order, items, productMap);
    }

    private Order saveOrderWithTotal(OrderRequest request, Map<String, ProductResponse> productMap) {
        double totalAmount = request.getItems().stream()
                .mapToDouble(item -> {
                    ProductResponse product = productMap.get(item.getProduct());
                    return product.getPrice() * item.getQuantity();
                })
                .sum();

        Order order = Order.builder()
                .email(request.getEmail())
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
                    item.setSubTotal(item.getUnitPrice() * item.getQuantity());
                    return item;
                })
                .collect(Collectors.toList());

        return orderItemRepository.saveAll(items);
    }

    private OrderResponse mapToOrderResponse(
            Order order, List<OrderItem> orderItems, Map<String, ProductResponse> productMap) {
        List<OrderItemResponse> responses = orderItemMapper.toOrderItemResponseList(orderItems, productMap);
        return orderMapper.toOrderResponse(order, responses);
    }

    private Map<String, ProductResponse> fetchProductData(List<String> productIds) {
        if (productIds.isEmpty()) return Collections.emptyMap();

        ApiResponse<List<ProductResponse>> response = productServiceClient.getProducts(
                ProductBatchRequest.builder().productIds(productIds).build());

        return response.getData().stream().collect(Collectors.toMap(ProductResponse::getId, Function.identity()));
    }
}
