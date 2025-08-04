package org.example.orderservice.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.example.orderservice.dto.response.ErrorResponse;
import org.example.orderservice.exception.AppException;
import org.example.orderservice.exception.ErrorCode;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeignErrorDecoder implements ErrorDecoder {
    ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            try {
                String responseBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
                throw new AppException(ErrorCode.findByCode(errorResponse.getCode()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new Default().decode(methodKey, response);
    }
}
