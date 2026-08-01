package com.spring.staymanager.config;


import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            public void addCorsMappings(@NonNull CorsRegistry registry){
                registry
                        .addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowCredentials(true)
                        .maxAge(3600)
                ;
            }
        };
    }


}
