package org.example.orderservice.dto.request;

import java.util.List;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @Valid
    List<OrderItemRequest> items;
}
