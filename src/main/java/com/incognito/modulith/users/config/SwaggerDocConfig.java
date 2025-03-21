package com.incognito.modulith.users.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerDocConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI();
    }
}
