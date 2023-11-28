package elice.team5th.controller

import elice.team5th.domain.auth.annotation.CurrentUser
import elice.team5th.domain.auth.entity.UserPrincipal
import elice.team5th.domain.like.dto.LikeDto
import elice.team5th.domain.like.service.LikeService
import elice.team5th.domain.tmdb.dto.MovieDto
import elice.team5th.domain.tmdb.dto.TVShowDto
import elice.team5th.domain.tmdb.enumtype.ContentType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/likes")
class LikeController(
    private val likeService: LikeService
) {
    @Operation(summary = "해당 유저의 선호 영화 목록 조회", description = "유저의 선호 영화 목록을 페이징 조회합니다.")
    @GetMapping("/movie/{user_id}")
    fun getLikedMoviesByUserId(
        @PathVariable("user_id") userId: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "20") size: Int
    ): ResponseEntity<Page<MovieDto.Response>> {
        val movies = likeService.getLikedMoviesByUserId(userId, page, size)
        return ResponseEntity.ok().body(
            movies.map {
                MovieDto.Response(it)
            }
        )
    }

    @Operation(summary = "해당 유저의 선호 TV 프로그램 목록 조회", description = "유저의 선호 TV 프로그램 목록을 페이징 조회합니다.")
    @GetMapping("/tv-show/{user_id}")
    fun getLikedTVShowsByUserId(
        @PathVariable("user_id") userId: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "20") size: Int
    ): ResponseEntity<Page<TVShowDto.Response>> {
        val tvShows = likeService.getLikedTVShowsByUserId(userId, page, size)
        return ResponseEntity.ok().body(
            tvShows.map {
                TVShowDto.Response(it)
            }
        )
    }

    @Operation(summary = "좋아요 기능", description = "로그인한 유저가 특정 컨텐츠를 좋아요 표시합니다.")
    @PostMapping("")
    fun likeContent(
        @CurrentUser userPrincipal: UserPrincipal,
        @RequestParam("content_type") contentType: ContentType,
        @RequestParam("content_id") contentId: Int
    ): ResponseEntity<LikeDto.Response> {
        val userId = userPrincipal.userId
        val like = likeService.likeContent(userId, contentType, contentId)
        return ResponseEntity.ok().body(LikeDto.Response(like!!))
    }

    @Operation(summary = "좋아요 취소 기능", description = "로그인한 유저가 특정 컨텐츠의 좋아요를 취소합니다.")
    @DeleteMapping("")
    fun cancelLike(
        @CurrentUser userPrincipal: UserPrincipal,
        @Parameter(description = "컨텐츠 타입 ENUM", example = "TV/MOVIE") @RequestParam("content_type") contentType: ContentType,
        @Parameter(description = "컨텐츠 ID", example = "502356") @RequestParam("content_id") contentId: Int
    ): ResponseEntity<String> {
        val userId = userPrincipal.userId
        likeService.cancelLike(userId, contentType, contentId)
        return ResponseEntity.ok().body("좋아요 취소 완료")
    }
}
