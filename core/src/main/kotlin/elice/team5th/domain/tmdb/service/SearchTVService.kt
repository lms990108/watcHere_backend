package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.SearchTVListInfoDto
import elice.team5th.domain.tmdb.dto.SearchTVListResponseDto
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class SearchTVService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun searchContent(query: String,  page: Int): Mono<SearchTVListResponseDto> {

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/search/tv")
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
                    SearchTVListInfoDto(
                        id = rawItem["id"] as Int,
                        name = rawItem["name"] as String?,
                        poster_path = "https://image.tmdb.org/t/p/w500${rawItem["poster_path"] as String}"
                    )
                }
                SearchTVListResponseDto(totalResults, results)
            }
    }
}
