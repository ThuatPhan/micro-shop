package org.example.cartservice.mapper;

import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.ProductResponse;
import org.example.cartservice.entity.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {
    CartItem toCartItem(CartItemRequest request);

    @Mapping(target = "product", expression = "java(product)")
    CartItemResponse toCartItemResponse(CartItem cartItem, @Context ProductResponse product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget CartItem cartItem, CartItemRequest request);
}
