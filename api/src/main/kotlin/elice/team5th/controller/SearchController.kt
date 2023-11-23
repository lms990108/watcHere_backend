package elice.team5th.controller

import elice.team5th.domain.tmdb.dto.ListResponseDto
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.tmdb.service.SearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/v1/search")
class SearchController(private val searchService: SearchService) {

    @GetMapping
    fun search(
        @RequestParam query: String,
        @RequestParam contentType: ContentType,
        @RequestParam(required = false, defaultValue = "1") page: Int // 페이징을 위한 매개변수 추가
    ): Mono<ListResponseDto> {
        return searchService.searchContent(query, contentType, page)
    }
}
