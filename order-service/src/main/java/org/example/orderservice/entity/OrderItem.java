package org.example.orderservice.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, updatable = false)
    Long quantity;

    @Column(nullable = false, updatable = false)
    Double unitPrice;

    @Column(name = "product_id", updatable = false)
    String productId;

    @ManyToOne
    @JoinColumn(name = "order_id", updatable = false)
    Order order;
}
