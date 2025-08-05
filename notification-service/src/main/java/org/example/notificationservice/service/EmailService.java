package org.example.notificationservice.service;

import org.example.notificationservice.dto.OrderResponse;

public interface EmailService {
    void sendOrderConfirmationEmail(OrderResponse order);
}
