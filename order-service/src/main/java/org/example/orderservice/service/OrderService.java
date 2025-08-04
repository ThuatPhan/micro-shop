package org.example.orderservice.service;

import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
}
