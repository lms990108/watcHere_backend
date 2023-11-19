package elice.team5th.controller

import elice.team5th.domain.tmdb.dto.ListResponseDto
import elice.team5th.domain.tmdb.netflix.NetflixKoreaPopularService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/netflix")
class NetflixController(private val netflixKoreaPopularService: NetflixKoreaPopularService) {

    @GetMapping("/popular-movie")
    fun getPopularMoviesInKorea(
        @RequestParam(required = false, defaultValue = "1") page: Int
    ): Mono<ResponseEntity<ListResponseDto>> {
        return netflixKoreaPopularService.getPopularMoviesInKorea(page)
            .map { ResponseEntity.ok(it) } // 반환 타입을 ResponseEntity<ListResponseDto>로 맞춥니다.
    }

}
