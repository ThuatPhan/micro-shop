package org.example.productservice.service;

import org.example.productservice.dto.request.CategoryCreationRequest;
import org.example.productservice.dto.request.CategoryUpdateRequest;
import org.example.productservice.dto.response.CategoryResponse;
import org.example.productservice.dto.response.PageResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreationRequest request);

    CategoryResponse updateCategory(String categoryId, CategoryUpdateRequest request);

    CategoryResponse getCategory(String categoryId);

    PageResponse<CategoryResponse> getCategories(int page, int size);

    void deleteCategory(String categoryId);
}
