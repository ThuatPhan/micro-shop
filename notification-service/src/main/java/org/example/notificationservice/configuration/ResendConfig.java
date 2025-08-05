package org.example.notificationservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.resend.Resend;

@Configuration
public class ResendConfig {
    @Value("${app.resend_api_key}")
    String API_KEY;

    @Bean
    public Resend resend() {
        return new Resend(API_KEY);
    }
}
