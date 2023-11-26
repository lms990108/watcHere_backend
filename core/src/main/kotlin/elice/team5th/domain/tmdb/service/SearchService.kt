package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.ListInfoDto
import elice.team5th.domain.tmdb.dto.SearchListResponseDto
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class SearchService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun searchContent(query: String, contentType: ContentType, page: Int): Mono<SearchListResponseDto> {
        val path = when (contentType) {
            ContentType.MOVIE -> "/search/movie"
            ContentType.TV -> "/search/tv"
        }

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(path)
                    .queryParam("query", query)
                    .queryParam("language", "ko-KR")
                    .queryParam("page", page)
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(Map::class.java)
            .onErrorMap(ErrorUtil::handleCommonErrors)
            .map { rawResponse ->
                @Suppress("UNCHECKED_CAST")
                val rawResults = rawResponse["results"] as List<Map<String, Any>>
                val totalResults = (rawResponse["total_results"] as Number).toInt() // 총 결과 개수 추가

                val results = rawResults.map { rawItem ->
                    ListInfoDto(
                        id = rawItem["id"] as Int,
                        title = if (contentType == ContentType.MOVIE) rawItem["title"] as String? else rawItem["name"] as String?,
                        name = if (contentType == ContentType.TV) rawItem["name"] as String? else rawItem["title"] as String?,
                        poster_path = "https://image.tmdb.org/t/p/w500${rawItem["poster_path"] as String}"
                    )
                }
                SearchListResponseDto(totalResults, results)
            }
    }
}
