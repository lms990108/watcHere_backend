package elice.team5th.controller

import elice.team5th.domain.clicks.service.ContentClickService
import elice.team5th.domain.tmdb.dto.TVDetailsDto
import elice.team5th.domain.tmdb.service.TVDetailsService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/tv")
class TVDetailsController(
    private val tvDetailsService: TVDetailsService,
    private val contentClickService: ContentClickService // 서비스 주입
) {

    @GetMapping("/{tvId}")
    @Operation(
        summary = "Tv 상세조회",
        description = "Tv id를 통해 Tvshow 상세조회"
    )
    fun getTVDetails(@PathVariable tvId: Int): Mono<ResponseEntity<TVDetailsDto>> {

        return tvDetailsService.getTVDetails(tvId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }
}
