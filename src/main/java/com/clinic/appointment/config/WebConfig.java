package com.clinic.appointment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * WebConfig: Configure web resources and file upload handling
 * Allows serving uploaded files from the file system
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload.dir:uploads/doctors}")
    private String uploadDir;

    /**
     * Configure resource handlers for serving uploaded files
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().toUri().toString();
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath)
                .setCachePeriod(3600);
    }
}
