package org.example.orderservice.controller;

import jakarta.validation.Valid;

import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.response.ApiResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), orderService.createOrder(request));
    }
}
