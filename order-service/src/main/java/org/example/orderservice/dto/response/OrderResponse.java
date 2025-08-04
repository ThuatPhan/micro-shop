package org.example.orderservice.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    List<OrderItemResponse> items;
    Instant createdAt;
}
