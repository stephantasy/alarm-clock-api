package com.stephantasy.alarmclock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    private static final String[] ALLOWED_ORIGINS = {
            "*"
    };

    private static final String[] ALLOWED_HEADERS = {
//            "Access-Control-Allow-Origin",
//            "Access-Control-Allow-Headers",
//            "Access-Control-Request-Method",
//            "Access-Control-Request-Headers",
//            "Content-Type",
//            "Accept",
//            "Origin",
//            "Authorization",
//            "X-Requested-With"
            "*"
    };

    private static final String[] ALLOWED_METHODS = {
            "GET",
            "PUT",
            "POST",
            "DELETE",
            "HEAD",
            "PATCH",
            "OPTIONS"
    };

    private static final String[] EXPOSED_HEADERS = {
            "Content-Disposition"
    };

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(ALLOWED_ORIGINS)
                        .allowedHeaders(ALLOWED_HEADERS)
                        .allowedMethods(ALLOWED_METHODS)
                        .allowCredentials(true)
                        .exposedHeaders(EXPOSED_HEADERS)
                        .maxAge(3000);
            }
        };
    }
}
