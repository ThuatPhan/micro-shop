package org.example.notificationservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    String id;
    ProductResponse product;
    Long quantity;
    Double unitPrice;
    Double subTotal;
}
