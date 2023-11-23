package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.ListInfoDto
import elice.team5th.domain.tmdb.dto.ListResponseDto
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.tmdb.enumtype.ProviderType
import elice.team5th.domain.tmdb.enumtype.SortType
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class ContentListService(private val webClient: WebClient) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getPopularContent(page: Int, sortType: SortType, providerName: String, contentType: ContentType): Mono<ListResponseDto> {
        val path = when (contentType) {
            ContentType.MOVIE -> "/discover/movie"
            ContentType.TV -> "/discover/tv"
        }

        println("Path: $path") // 로그 출력

        val providerId = providerName.let {
            val id = ProviderType.getIdByName(it)
            println("Provider ID: $id") // 로그 출력
            id
        }

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(path)
                    .queryParam("include_adult", "false")
                    .queryParam("include_video", "false")
                    .queryParam("language", "ko-KR")
                    .queryParam("page", page.toString())
                    .queryParam("sort_by", sortType.queryParam)
                    .queryParam("watch_region", "KR")
                    .apply {
                        providerId?.let {
                            queryParam("with_watch_providers", it)
                        }
                    }
                    .build()
                    .also { println("URI: $it") } // 로그 출력
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(ListResponseDto::class.java)
            .onErrorMap(ErrorUtil::handleCommonErrors)
            .map { response ->
                println("Response: $response") // 로그 출력
                ListResponseDto(
                    results = response.results.map { content ->
                        ListInfoDto(
                            id = content.id,
                            title = if (contentType == ContentType.MOVIE) content.title else null,
                            name = if (contentType == ContentType.TV) content.name else null,
                            poster_path = "https://image.tmdb.org/t/p/w500${content.poster_path}"
                        ).also { println("Mapped Content: $it") } // 로그 출력
                    }
                )
            }
    }
}
