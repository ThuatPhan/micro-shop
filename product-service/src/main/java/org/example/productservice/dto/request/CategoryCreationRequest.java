package org.example.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than or equal to 255 characters")
    String name;

    @Size(min = 5, max = 510, message = "Description must be between 5 and 510 characters")
    String description;
}
