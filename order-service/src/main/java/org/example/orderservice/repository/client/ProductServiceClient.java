package org.example.orderservice.repository.client;

import java.util.List;

import org.example.orderservice.configuration.AuthenticationRequestInterceptor;
import org.example.orderservice.dto.request.ProductBatchRequest;
import org.example.orderservice.dto.response.ApiResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// spotless:off
@FeignClient(
        name = "product-service",
        path = "/internal/products",
        configuration = {AuthenticationRequestInterceptor.class}
)
// spotless:on
public interface ProductServiceClient {
    @GetMapping("/{productId}")
    ApiResponse<ProductResponse> getProduct(@PathVariable String productId);

    @PostMapping
    ApiResponse<List<ProductResponse>> getProducts(@RequestBody ProductBatchRequest request);
}
