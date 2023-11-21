package elice.team5th.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("Elice 5th API")
                .description("Elice 5th API Docs")
                .version("v1.0.0")
        )
    }
}
