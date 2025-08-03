package org.example.productservice.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name;

    @Size(min = 5, max = 510, message = "Description must be between 5 and 510 characters")
    String description;

    @Positive(message = "Price must be a positive number")
    Double price;

    String image;

    String category;
}
