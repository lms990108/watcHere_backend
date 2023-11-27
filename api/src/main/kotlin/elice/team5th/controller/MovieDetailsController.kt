package elice.team5th.controller

import elice.team5th.domain.clicks.service.ContentClickService
import elice.team5th.domain.tmdb.dto.MovieDetailsDto
import elice.team5th.domain.tmdb.service.MovieDetailsService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/movie")
class MovieDetailsController(
    private val movieDetailsService: MovieDetailsService,
    private val contentClickService: ContentClickService // 추가
) {

    @GetMapping("/{movieId}")
    @Operation(
        summary = "영화 상세조회",
        description = "영화 id를 통해 영화 상세조회"
    )
    fun getMovieDetails(@PathVariable movieId: Int): Mono<ResponseEntity<MovieDetailsDto>> {
        println(movieId)

        return movieDetailsService.getMovieDetails(movieId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }
}
