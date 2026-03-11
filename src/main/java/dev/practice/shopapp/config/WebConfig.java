package dev.practice.shopapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**") // Apply to all your API endpoints
                .allowedOrigins(
                        "http://localhost:5173",            // Local Vite development
                        "https://your-app-name.netlify.app" // Your future Netlify URL
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Standard REST methods
                .allowedHeaders("*") // Allow all headers (like Authorization or Content-Type)
                .allowCredentials(true) // Required if you later add cookies or specific auth
                .maxAge(3600); // Cache the pre-flight request for 1 hour
    }
}
