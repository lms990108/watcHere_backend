package elice.team5th.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Value("\${tmdb.api.base-url}")
    private lateinit var baseUrl: String

    @Bean
    fun webClient(): WebClient {
        return WebClient
            .builder()
            .baseUrl(baseUrl)
            .build()
    }
}
