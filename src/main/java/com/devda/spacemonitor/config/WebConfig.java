package com.devda.spacemonitor.config;

import com.devda.spacemonitor.service.ImageDatasetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import java.nio.file.Path;
import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ImageDatasetService imageDatasetService;
    private final String allowedOrigins;

    public WebConfig(ImageDatasetService imageDatasetService,
                     @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
                     String allowedOrigins) {
        this.imageDatasetService = imageDatasetService;
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim).toArray(String[]::new);
        registry.addMapping("/api/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path directory = imageDatasetService.getDatasetDirectory();
        String location = directory.toUri().toString();
        if (!location.endsWith("/")) location += "/";
        registry.addResourceHandler("/media/**").addResourceLocations(location);
    }
}
