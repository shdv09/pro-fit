package com.shdv09.webapplication.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Fitness Club Management System Api",
                description = "Pro-Fit System", version = "1.0.0",
                contact = @Contact(
                        name = "Scherbatykh Denis",
                        email = "shdv09@gmail.com"
                )
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@SecurityScheme(
        name = "basic",
        type = SecuritySchemeType.HTTP,
        scheme = "basic")
public class OpenApiConfig {
}
