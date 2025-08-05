package org.example.notificationservice.service;

import java.util.HashMap;
import java.util.Map;

import org.example.notificationservice.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {
    Resend resend;
    TemplateEngine templateEngine;

    @NonFinal
    @Value("${app.email_domain}")
    String EMAIL_DOMAIN;

    @Override
    public void sendOrderConfirmationEmail(OrderResponse order) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("order", order);

        String html = buildTemplate("order-confirmation", variables);
        String to = order.getEmail();
        String subject = "Order Confirmation";

        sendEmail(to, subject, html);
    }

    private String buildTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process(templateName, context);
    }

    private void sendEmail(String to, String subject, String html) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("MicroShop <no-reply@" + EMAIL_DOMAIN + ">")
                .to(to)
                .subject(subject)
                .html(html)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            log.error("Error sending email: {}", e.getMessage());
        }

        log.info("Email sent to {} with subject '{}'", to, subject);
    }
}
