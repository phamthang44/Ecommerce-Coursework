package com.greenwich.ecommerce.infra.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class AppConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://localhost:5173",
                                "http://localhost:5500",
                                "http://localhost",
                                "http://127.0.0.1",
                                "http://100.81.52.73",
                                "http://100.108.47.47",
                                "http://26.16.186.88",
                                "http://100.84.108.21",
                                "http://26.95.104.142",
                                "http://26.39.211.253",
                                "http://26.213.96.220",
                                "https://thang.tail704409.ts.net"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
