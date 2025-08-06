package org.example.notificationservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String description;
    Double price;
    String image;
    CategoryResponse category;
}
