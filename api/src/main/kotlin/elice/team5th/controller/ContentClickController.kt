package elice.team5th.controller

import elice.team5th.domain.clicks.dto.MovieWithClicksDto
import elice.team5th.domain.clicks.dto.TVShowWithClicksDto
import elice.team5th.domain.clicks.service.ContentClickService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/topclick")
class ContentClickController(private val contentClickService: ContentClickService) {

    // 인기 영화 목록 가져오기 (페이징 처리)
    @Operation(
        summary = "영화 조회순"
    )
    @GetMapping("/movies")
    fun getTopMoviesByClicks(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): Page<MovieWithClicksDto> {
        return contentClickService.getTopMoviesByClicks(page, size)
    }

    // 인기 TV 쇼 목록 가져오기 (페이징 처리)
    @Operation(
        summary = "tv 조회순"
    )
    @GetMapping("/tvshows")
    fun getTopTVShowsByClicks(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): Page<TVShowWithClicksDto> {
        return contentClickService.getTopTVShowsByClicks(page, size)
    }
}
