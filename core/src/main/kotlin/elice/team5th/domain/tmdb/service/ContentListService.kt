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

    fun getPopularContent(page: Int, sortType: SortType, providerName: String, contentType: ContentType, anime: Boolean): Mono<ListResponseDto> {
        val path = when (contentType) {
            ContentType.MOVIE -> "/discover/movie"
            ContentType.TV -> "/discover/tv"
        }

        println("Path: $path") // 로그 출력

        val providerId = providerName.let {
            val id = ProviderType.getIdByName(it)
            id
        }

        val genresParam = if (anime) "with_genres" else "without_genres"

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(path)
                    .queryParam("include_adult", "false")
                    .queryParam("include_video", "false")
                    .queryParam("language", "ko-KR")
                    .queryParam("page", page.toString())
                    .queryParam("sort_by", sortType.queryParam)
                    .queryParam("watch_region", "KR")
                    .queryParam(genresParam, "16") // 애니메이션 관련 쿼리 파라미터 추가
                    .apply {
                        providerId?.let {
                            queryParam("with_watch_providers", it)
                        }
                    }
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(ListResponseDto::class.java)
            .onErrorMap(ErrorUtil::handleCommonErrors)
            .map { response ->
                ListResponseDto(
                    results = response.results.map { content ->
                        ListInfoDto(
                            id = content.id,
                            title = content.title ?: content.name, // 영화든 TV든 'title' 필드에 제목을 저장
                            name = null,
                            poster_path = "https://image.tmdb.org/t/p/w500${content.poster_path}"
                        )
                    }
                )
            }
    }
}
