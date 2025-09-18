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
                        "https://consorcio-front.sp1.br.saveincloud.net.br",
                        // Adicione os hostnames que aparecem no seu frontend
                        "https://disk-functionality-informal-markers.trycloudflare.com",
                        "https://repeated-transformation-tip-gap.trycloudflare.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight por 1 hora
    }
}
