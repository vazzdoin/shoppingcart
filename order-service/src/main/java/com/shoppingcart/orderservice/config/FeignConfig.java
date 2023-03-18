package com.shoppingcart.orderservice.config;

import com.shoppingcart.orderservice.external.decoder.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    /*
        Bean of the Custom Error decoder : will return the bean
        when required Custom Error decoder
        Tell spring to use the CustomErrorDecoder instead of default ErrorDecoder
    */
    @Bean
    ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

}
