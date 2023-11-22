package elice.team5th.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val devServer = Server()
            .url("http://kdt-sw-6-team05.elicecoding.com")
            .description("Dev Server")
        val localServer = Server()
            .url("http://localhost:8080")
            .description("Local Server")
        return OpenAPI()
            .info(
            Info()
                .title("Elice 5th API")
                .description("Elice 5th API Docs")
                .version("v1.0.0")
        )
            .servers(listOf(devServer, localServer))
    }
}
