package elice.team5th.controller

import elice.team5th.domain.clicks.entity.MovieClicksEntity
import elice.team5th.domain.clicks.entity.TVShowClicksEntity
import elice.team5th.domain.clicks.service.ContentClickService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/content")
class ContentClickController(private val contentClickService: ContentClickService) {

    // 인기 영화 목록 가져오기
    @GetMapping("/movies/top")
    fun getTopMoviesByClicks(): List<MovieClicksEntity> {
        return contentClickService.getTopMoviesByClicks()
    }

    // 인기 TV 쇼 목록 가져오기
    @GetMapping("/tvshows/top")
    fun getTopTVShowsByClicks(): List<TVShowClicksEntity> {
        return contentClickService.getTopTVShowsByClicks()
    }
}
