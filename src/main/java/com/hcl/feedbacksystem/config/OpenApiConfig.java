package com.hcl.feedbacksystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI feedbackSystemOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Feedback System API")
                        .version("v1")
                        .description("REST API for employee onboarding, JWT authentication, and peer feedback workflows.")
                        .contact(new Contact().name("Feedback System Team"))
                        .license(new License().name("Internal Use")))
                .servers(List.of(new Server().url("/").description("Current environment")))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}