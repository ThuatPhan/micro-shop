package org.example.productservice.service;

import org.example.productservice.dto.request.ProductCreationRequest;
import org.example.productservice.dto.request.ProductUpdateRequest;
import org.example.productservice.dto.response.PageResponse;
import org.example.productservice.exception.AppException;
import org.example.productservice.exception.ErrorCode;
import org.example.productservice.mapper.ProductMapper;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.repository.CategoryRepository;
import org.example.productservice.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {
        boolean isProductExists = productRepository.existsByName(request.getName());
        if (isProductExists) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        var product = productMapper.toProduct(request);

        var category = categoryRepository
                .findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProduct(String productId) {
        var product =
                productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return productMapper.toProductResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getProducts(int page, int size) {
        var productsPage = productRepository.findAll(PageRequest.of(page, size));
        return PageResponse.of(
                productsPage.hasNext(),
                productsPage.map(productMapper::toProductResponse).toList());
    }

    @Override
    public ProductResponse updateProduct(String productId, ProductUpdateRequest request) {
        boolean isProductExists = productRepository.existsByNameAndIdNot(request.getName(), productId);
        if (isProductExists) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        var product =
                productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.partialUpdate(product, request);

        var category = categoryRepository
                .findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(String productId) {
        boolean isProductExists = productRepository.existsById(productId);
        if (!isProductExists) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        productRepository.deleteById(productId);
    }
}
