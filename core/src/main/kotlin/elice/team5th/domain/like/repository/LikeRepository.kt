package elice.team5th.domain.like.repository

import elice.team5th.domain.like.model.Like
import elice.team5th.domain.tmdb.entity.MovieEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long> {
    fun findByUserUserIdAndMovieIsNotNull(userId: String, pageable: Pageable): Page<Like>
    fun findByUserUserIdAndTvShowIsNotNull(userId: String, pageable: Pageable): Page<Like>
    fun deleteByUserUserIdAndMovie(userId: String, movie: MovieEntity)
    fun deleteByUserUserIdAndTvShow(userId: String, tvShow: TVShowEntity)
}
