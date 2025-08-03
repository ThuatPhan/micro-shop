package org.example.productservice.mapper;

import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.dto.request.ProductCreationRequest;
import org.example.productservice.dto.request.ProductUpdateRequest;
import org.example.productservice.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductCreationRequest request);

    ProductResponse toProductResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Product product, ProductUpdateRequest request);
}
