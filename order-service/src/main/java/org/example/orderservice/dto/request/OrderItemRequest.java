package org.example.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {
    @NotBlank(message = "Product is required")
    String product;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Long quantity;

    @NotNull(message = "Unit price cannot be null")
    @Min(value = 0, message = "UnitPrice must be non-negative")
    Double unitPrice;
}
