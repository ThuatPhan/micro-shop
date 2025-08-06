package org.example.notificationservice.dto;

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
    String email;
    Double totalAmount;
    Instant createdAt;
    List<OrderItemResponse> items;
}
