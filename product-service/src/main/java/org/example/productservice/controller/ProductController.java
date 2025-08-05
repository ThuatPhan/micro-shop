package org.example.productservice.controller;

import jakarta.validation.Valid;

import org.example.productservice.dto.request.ProductCreationRequest;
import org.example.productservice.dto.request.ProductUpdateRequest;
import org.example.productservice.dto.response.ApiResponse;
import org.example.productservice.dto.response.PageResponse;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Tag(name = "Product API")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductCreationRequest request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), productService.createProduct(request));
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable String productId) {
        return ApiResponse.success(HttpStatus.OK.value(), productService.getProduct(productId));
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(HttpStatus.OK.value(), productService.getProducts(page, size));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable String productId, @RequestBody @Valid ProductUpdateRequest request) {
        return ApiResponse.success(HttpStatus.OK.value(), productService.updateProduct(productId, request));
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
    }
}
