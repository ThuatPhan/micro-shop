package org.example.cartservice.entity;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "product_id")
    String productId;

    Long quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart;
}
