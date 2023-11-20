package elice.team5th.controller

import elice.team5th.domain.tmdb.dto.MovieDetailsDto
import elice.team5th.domain.tmdb.service.MovieDetailsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/movie")
class MovieDetailsController(private val movieDetailsService: MovieDetailsService) {

    @GetMapping("/{movieId}")
    fun getMovieDetails(@PathVariable movieId: Int): Mono<ResponseEntity<MovieDetailsDto>> {
        println(movieId)
        return movieDetailsService.getMovieDetails(movieId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }
}
