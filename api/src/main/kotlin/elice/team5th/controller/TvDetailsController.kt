package elice.team5th.controller

import elice.team5th.domain.tmdb.dto.TVDetailsDto
import elice.team5th.domain.tmdb.service.TVDetailsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/tv")
class TVDetailsController(private val tvDetailsService: TVDetailsService) {

    @GetMapping("/{tvId}")
    fun getTVDetails(@PathVariable tvId: Int): Mono<ResponseEntity<TVDetailsDto>> {
        return tvDetailsService.getTVDetails(tvId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }
}
