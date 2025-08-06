package org.example.orderservice.repository;

import java.util.List;

import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItem> findByOrder(Order order);
}
