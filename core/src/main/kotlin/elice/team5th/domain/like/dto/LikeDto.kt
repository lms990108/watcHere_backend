package elice.team5th.domain.like.dto

import elice.team5th.domain.like.model.Like
import elice.team5th.domain.tmdb.entity.MovieEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import elice.team5th.domain.user.model.User
import java.time.LocalDateTime

class LikeDto {
    data class Response(
        val id: Long,
        val user: User,
        val movie: MovieEntity?,
        val tvShow: TVShowEntity?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        constructor(like: Like) : this(
            id = like.id,
            user = like.user,
            movie = like.movie,
            tvShow = like.tvShow,
            createdAt = like.createdAt,
            updatedAt = like.updatedAt
        )
    }

    data class LikeRequest(
        val movieId: Long?,
        val tvShowId: Long?
    )
}
