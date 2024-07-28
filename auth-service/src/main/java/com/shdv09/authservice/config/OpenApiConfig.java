package com.shdv09.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Authorization Service Api",
                description = "Authorization Service", version = "1.0.0",
                contact = @Contact(
                        name = "Scherbatykh Denis",
                        email = "shdv09@gmail.com"
                )
        )
)
@SecurityScheme(
        name = "basic",
        type = SecuritySchemeType.HTTP,
        scheme = "basic")
public class OpenApiConfig {
}
