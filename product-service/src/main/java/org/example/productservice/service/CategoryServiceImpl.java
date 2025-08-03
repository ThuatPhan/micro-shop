package org.example.productservice.service;

import org.example.productservice.dto.request.CategoryCreationRequest;
import org.example.productservice.dto.request.CategoryUpdateRequest;
import org.example.productservice.dto.response.CategoryResponse;
import org.example.productservice.dto.response.PageResponse;
import org.example.productservice.exception.AppException;
import org.example.productservice.exception.ErrorCode;
import org.example.productservice.mapper.CategoryMapper;
import org.example.productservice.repository.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @PreAuthorize("isAuthenticated()")
    @Override
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        boolean isCategoryExists = categoryRepository.existsByName(request.getName());
        if (isCategoryExists) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        var category = categoryMapper.toCategory(request);

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getCategory(String categoryId) {
        var category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public PageResponse<CategoryResponse> getCategories(int page, int size) {
        var categoriesPage = categoryRepository.findAll(PageRequest.of(page, size));
        return PageResponse.of(
                categoriesPage.hasNext(),
                categoriesPage.map(categoryMapper::toCategoryResponse).toList());
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public CategoryResponse updateCategory(String categoryId, CategoryUpdateRequest request) {
        boolean isCategoryExists = categoryRepository.existsByNameAndIdNot(request.getName(), categoryId);
        if (isCategoryExists) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        var category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryMapper.partialUpdate(category, request);

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public void deleteCategory(String categoryId) {
        boolean isCategoryExists = categoryRepository.existsById(categoryId);
        if (!isCategoryExists) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        categoryRepository.deleteById(categoryId);
    }
}
