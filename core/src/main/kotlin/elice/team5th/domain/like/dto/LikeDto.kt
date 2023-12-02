package elice.team5th.domain.like.dto

import elice.team5th.domain.like.model.Like
import elice.team5th.domain.tmdb.dto.MovieDto
import elice.team5th.domain.tmdb.dto.TVShowDto
import elice.team5th.domain.tmdb.entity.MovieEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.model.User
import java.time.LocalDateTime

class LikeDto {
    data class Response(
        val id: Long,
        val user: UserDto.Response,
        var movie: MovieDto.Response? = null,
        var tvShow: TVShowDto.Response? = null,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        constructor(like: Like) : this(
            id = like.id,
            user = UserDto.Response(like.user),
            movie = like.movie?.let { MovieDto.Response(it) },
            tvShow = like.tvShow?.let { TVShowDto.Response(it) },
            createdAt = like.createdAt,
            updatedAt = like.updatedAt
        )
    }
}
