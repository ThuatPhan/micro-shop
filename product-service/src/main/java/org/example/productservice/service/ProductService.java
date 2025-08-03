package org.example.productservice.service;

import org.example.productservice.dto.request.ProductCreationRequest;
import org.example.productservice.dto.request.ProductUpdateRequest;
import org.example.productservice.dto.response.PageResponse;
import org.example.productservice.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductCreationRequest request);

    ProductResponse getProduct(String productId);

    PageResponse<ProductResponse> getProducts(int page, int size);

    ProductResponse updateProduct(String productId, ProductUpdateRequest request);

    void deleteProduct(String productId);
}
