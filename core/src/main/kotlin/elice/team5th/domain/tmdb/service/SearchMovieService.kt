package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.SearchMovieListResponseDto
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class SearchMovieService(
    private val webClient: WebClient,
    private val movieDetailsService: MovieDetailsService
) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    @Transactional
    fun searchContent(query: String, page: Int): Mono<SearchMovieListResponseDto> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/search/movie")
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
                val totalResults = (rawResponse["total_results"] as Number).toInt() // 총 결과 개수 추가
                val rawResults = rawResponse["results"] as List<Map<String, Any>>
                val ids = rawResults.map { it["id"] as Int }
                Flux.fromIterable(ids)
                    .flatMap { id ->
                        movieDetailsService.getMovieDetails(id)
                    }
                    .collectList()
                    .map { movieDetailsList ->
                        SearchMovieListResponseDto(totalResults, movieDetailsList)
                    }
            }
    }
}
