package elice.team5th.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val securityScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .name("JWT Authentication")

        val securityRequirement = SecurityRequirement().addList("jwtAuth")

        val info = Info().title("Elice 5th API").description("Elice 5th API Docs").version("v1.0.0")
        val devServer = Server().url("https://kdt-sw-6-team05.elicecoding.com").description("Dev Server")
        val localServer = Server().url("http://localhost:8080").description("Local Server")

        return OpenAPI()
            .info(info)
            .servers(listOf(devServer, localServer))
            .components(Components().addSecuritySchemes("jwtAuth", securityScheme))
            .addSecurityItem(securityRequirement)
    }
}
