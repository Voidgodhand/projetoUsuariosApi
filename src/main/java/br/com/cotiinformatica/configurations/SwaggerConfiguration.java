package br.com.cotiinformatica.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Usuários - COTI Informática Java WebDeveloper")
                .version("v1")
                .description("Documentação da API - Projeto Spring Boot com banco de dados PostGreSQL")
                .contact(new Contact()
                    .name("COTI Informática")
                    .email("contato@cotiinformatica.com.br")
                    .url("https://www.cotiinformatica.com.br")
                )
            );
    }
}