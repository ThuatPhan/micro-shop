package org.example.orderservice.controller;

import jakarta.validation.Valid;

import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.response.ApiResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Tag(name = "Order API")
@SecurityRequirement(name = "bearerAuth")
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

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable String orderId) {
        return ApiResponse.success(HttpStatus.OK.value(), orderService.getOrder(orderId));
    }
}
