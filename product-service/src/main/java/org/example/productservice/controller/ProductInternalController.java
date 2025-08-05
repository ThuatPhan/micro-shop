package org.example.productservice.controller;

import java.util.List;

import org.example.productservice.dto.request.ProductBatchRequest;
import org.example.productservice.dto.response.ApiResponse;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductInternalController {
    ProductService productService;

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable String productId) {
        return ApiResponse.success(HttpStatus.OK.value(), productService.getProduct(productId));
    }

    @PostMapping
    public ApiResponse<List<ProductResponse>> getProducts(@RequestBody ProductBatchRequest request) {
        return ApiResponse.success(HttpStatus.OK.value(), productService.getProducts(request));
    }
}
