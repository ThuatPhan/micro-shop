package org.example.orderservice.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.orderservice.dto.request.OrderItemRequest;
import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.entity.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    @Mapping(target = "productId", source = "product")
    OrderItem toOrderItem(OrderItemRequest request);

    @Mapping(target = "product", expression = "java(product)")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem, @Context ProductResponse product);

    /**
     * Map a list of OrderItem to OrderItemResponse using the provided product map.
     */
    default List<OrderItemResponse> toOrderItemResponseList(
            List<OrderItem> items, Map<String, ProductResponse> productMap) {
        return items.stream()
                .map(item -> {
                    ProductResponse product = productMap.get(item.getProductId());
                    return toOrderItemResponse(item, product);
                })
                .collect(Collectors.toList());
    }
}
