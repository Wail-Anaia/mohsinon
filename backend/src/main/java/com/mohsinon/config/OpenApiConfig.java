package com.mohsinon.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Mohsinon Platform API",
                version = "1.0.0",
                description = """
                        REST API الخاصة بمنصة محسنون.

                        منصة لإدارة العمل الخيري، المساجد، المتطوعين،
                        التبرعات، المشاريع، والصلاحيات الديناميكية.
                        """,
                contact = @Contact(
                        name = "Wail Anaia",
                        email = "wail.anaia04@gmail.com",
                        url = "https://github.com/Wail-Anaia"
                ),
                license = @License(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "أدخل JWT Token بدون كلمة Bearer"
)
public class OpenApiConfig {
}