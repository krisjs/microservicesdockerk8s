package com.learning.OrderService.config;

import com.learning.OrderService.external.decoder.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
public class FeignConfig {
    @Bean
    ErrorDecoder errorDecoder() {
        return  new CustomErrorDecoder();
    }
}
