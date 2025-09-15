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
                        "http://localhost:4200", // Para 'ng serve' local
                        "http://localhost:8081", // Para o Docker Compose local
                        "http://192.168.49.2:30081", // Para o acesso via Minikube NodePort
                        "http://192.168.40.70:30081" // Para o seu k3s NodePort - ADICIONE ESTA LINHA
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}