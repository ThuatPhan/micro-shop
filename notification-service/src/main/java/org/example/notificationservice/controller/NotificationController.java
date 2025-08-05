package org.example.notificationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.constant.RabbitMQConstant;
import org.example.notificationservice.dto.OrderResponse;
import org.example.notificationservice.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    ObjectMapper objectMapper;
    EmailService emailService;

    @RabbitListener(queues = RabbitMQConstant.QUEUE)
    public void listenNotificationQueue(String message) {
        try {
            OrderResponse order = objectMapper.readValue(message, OrderResponse.class);
            emailService.sendOrderConfirmationEmail(order);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON message: {}", e.getMessage());
        }
    }
}
