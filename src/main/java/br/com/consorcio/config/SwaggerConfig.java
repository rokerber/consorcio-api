package br.com.consorcio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info = @Info(title = "Projeto Consorcio API", version = "V1",
                                description = "Projeto Consorcio responsável por simular cenarios de investimento"))
public class SwaggerConfig {

    @Bean
    public OpenAPI informacaoApi() {
        return new OpenAPI().components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info().title("Consorcio API")
                        .version("V1")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}