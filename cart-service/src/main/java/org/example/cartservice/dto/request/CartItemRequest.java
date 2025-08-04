package org.example.cartservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    @NotBlank(message = "Product is required")
    String product;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be a positive number")
    Long quantity;
}
