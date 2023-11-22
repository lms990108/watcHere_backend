package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.TVDetailsDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class TVDetailsService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getTVDetails(tvId: Int): Mono<TVDetailsDto> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/tv/$tvId")
                    .queryParam("language", "ko-KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(TVDetailsDto::class.java)
    }
}
