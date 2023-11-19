package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.ListInfoDto
import elice.team5th.domain.tmdb.dto.ListResponseDto
import elice.team5th.domain.tmdb.enumtype.ProviderType
import elice.team5th.domain.tmdb.enumtype.SortType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class MovieListService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getPopularMoviesInKorea(page: Int, sortType: SortType, providerName: String): Mono<ListResponseDto> {
        val providerId = providerName.let { ProviderType.getIdByName(it) }

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/discover/movie")
                    .queryParam("include_adult", "false")
                    .queryParam("include_video", "false")
                    .queryParam("language", "ko-KR")
                    .queryParam("page", page.toString())
                    .queryParam("sort_by", sortType.queryParam)
                    .apply {
                        providerId?.let {
                            queryParam("with_watch_providers", it)
                        }
                    }
                    .queryParam("watch_region", "KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(ListResponseDto::class.java)
            .map { response ->
                ListResponseDto(
                    results = response.results.map { movie ->
                        ListInfoDto(
                            id = movie.id,
                            title = movie.title,
                            poster_path = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
                        )
                    }
                )
            }
    }
}
