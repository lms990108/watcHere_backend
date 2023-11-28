package elice.team5th.domain.like.repository

import elice.team5th.domain.like.model.Like
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long> {
    fun findByUserUserIdAndMovieIsNotNull(userId: String, pageable: Pageable): Page<Like>
    fun findByUserUserIdAndTvShowIsNotNull(userId: String, pageable: Pageable): Page<Like>
    fun findByUserUserIdAndMovieId(userId: String, movieId: Long): Like?
    fun findByUserUserIdAndTvShowId(userId: String, tvShowId: Long): Like?
}
