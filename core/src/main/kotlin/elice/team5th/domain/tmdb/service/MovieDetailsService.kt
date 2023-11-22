package elice.team5th.domain.tmdb.service

import com.fasterxml.jackson.core.JsonProcessingException
import elice.team5th.domain.tmdb.dto.MovieDetailsDto
import elice.team5th.domain.tmdb.exception.ResponseParsingException
import elice.team5th.domain.tmdb.exception.UnknownErrorException
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class MovieDetailsService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getMovieDetails(movieId: Int): Mono<MovieDetailsDto> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/movie/$movieId")
                    .queryParam("language", "ko-KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(MovieDetailsDto::class.java)
            .onErrorMap(ErrorUtil::handleCommonErrors)
    }
}
