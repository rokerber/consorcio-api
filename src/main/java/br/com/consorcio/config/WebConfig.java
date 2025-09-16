package br.com.consorcio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:4200",
                        "http://localhost:8081",
                        "http://192.168.40.70:30081",
                        "http://192.168.40.70:30085",
                        // URLs do Cloudflare
                        "https://kde-bill-owen-vice.trycloudflare.com", // consorcio-front-dev
                        "https://disk-functionality-informal-markers.trycloudflare.com" // consorcio-front-staging
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}