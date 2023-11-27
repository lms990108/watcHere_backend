package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.SearchTVListResponseDto
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
@Service
class SearchTVService(
    private val webClient: WebClient,
    private val tvDetailsService: TVDetailsService // TVDetailsService 주입
) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    @Transactional
    fun searchContent(query: String, page: Int): Mono<SearchTVListResponseDto> {
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
            .flatMap { rawResponse ->
                @Suppress("UNCHECKED_CAST")
                val totalResults = (rawResponse["total_results"] as Number).toInt()
                val rawResults = rawResponse["results"] as List<Map<String, Any>>
                val ids = rawResults.map { it["id"] as Int }
                Flux.fromIterable(ids)
                    .flatMap { id ->
                        tvDetailsService.getTVDetails(id) // 상세 정보 조회
                    }
                    .collectList()
                    .map { tvDetailsList ->
                        SearchTVListResponseDto(totalResults, tvDetailsList)
                    }
            }
    }
}
