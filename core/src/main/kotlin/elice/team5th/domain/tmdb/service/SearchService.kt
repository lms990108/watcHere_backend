package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.ListInfoDto
import elice.team5th.domain.tmdb.dto.ListResponseDto
import elice.team5th.domain.tmdb.enumtype.ContentType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class SearchService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun searchContent(query: String, contentType: ContentType): Mono<ListResponseDto> {
        val path = when (contentType) {
            ContentType.MOVIE -> "/search/movie"
            ContentType.TV -> "/search/tv"
        }

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(path)
                    .queryParam("query", query)
                    .queryParam("language", "ko-KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(Map::class.java)
            .map { rawResponse ->
                @Suppress("UNCHECKED_CAST")
                val rawResults = rawResponse["results"] as List<Map<String, Any>>
                ListResponseDto(
                    results = rawResults.map { rawItem ->
                        ListInfoDto(
                            id = rawItem["id"] as Int,
                            title = if (contentType == ContentType.MOVIE) rawItem["title"] as String? else null,
                            name = if (contentType == ContentType.TV) rawItem["name"] as String? else null,
                            poster_path = "https://image.tmdb.org/t/p/w500${rawItem["poster_path"] as String}"
                        )
                    }
                )
            }
    }
}
