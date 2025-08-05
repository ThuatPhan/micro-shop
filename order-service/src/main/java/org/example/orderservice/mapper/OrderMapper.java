package org.example.orderservice.mapper;

import java.util.List;

import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.entity.Order;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "items", expression = "java(items)")
    OrderResponse toOrderResponse(Order order, @Context List<OrderItemResponse> items);
}
