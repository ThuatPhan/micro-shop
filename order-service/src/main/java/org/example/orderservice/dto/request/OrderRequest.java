package org.example.orderservice.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Not valid Email format")
    String email;

    @Valid
    List<OrderItemRequest> items;
}
