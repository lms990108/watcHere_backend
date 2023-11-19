package elice.team5th.domain.tmdb.netflix

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class NetflixKoreaPopularService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getPopularMoviesInKorea(page: Int): Mono<String> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/discover/movie")
                    .queryParam("include_adult", "false")
                    .queryParam("include_video", "false")
                    .queryParam("language", "ko-KR")
                    .queryParam("page", page.toString())
                    .queryParam("sort_by", "popularity.desc")
                    .queryParam("watch_region", "KR")
                    .queryParam("with_watch_providers", "8")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(String::class.java)
    }
}


