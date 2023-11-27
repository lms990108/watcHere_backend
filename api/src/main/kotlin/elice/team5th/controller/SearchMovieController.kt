package elice.team5th.controller

import elice.team5th.domain.tmdb.dto.SearchMovieListResponseDto
import elice.team5th.domain.tmdb.service.SearchMovieService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/v1/search")
class SearchMovieController(private val searchMovieService: SearchMovieService) {

    @GetMapping
    @Operation(
        summary = "검색 기능",
        description = "제목을 한국어로 검색할 수 있습니다. \n" +
            "contentType을 명시해야합니다. MOVIE or TV" +
            "하나의 키워드로 타입다르게 두번 api 날리면 될듯합니다."
    )
    fun search(
        @RequestParam query: String,
        @RequestParam(required = false, defaultValue = "1") page: Int // 페이징을 위한 매개변수 추가
    ): Mono<SearchMovieListResponseDto> {
        return searchMovieService.searchContent(query, page)
    }
}
