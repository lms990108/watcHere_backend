package elice.team5th.domain.like.service

import elice.team5th.domain.like.dto.LikeDto
import elice.team5th.domain.like.exception.LikeNotFoundException
import elice.team5th.domain.like.model.Like
import elice.team5th.domain.like.repository.LikeRepository
import elice.team5th.domain.tmdb.entity.MovieEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import elice.team5th.domain.tmdb.exception.MovieNotFoundException
import elice.team5th.domain.tmdb.exception.TVShowNotFoundException
import elice.team5th.domain.tmdb.repository.MovieRepository
import elice.team5th.domain.tmdb.repository.TVShowRepository
import elice.team5th.domain.user.exception.UserNotFoundException
import elice.team5th.domain.user.model.User
import elice.team5th.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TVShowRepository
) {
    fun getLikedMoviesByUserId(userId: String, page: Int, size: Int): Page<MovieEntity> {
        val pageable = PageRequest.of(page, size)
        val likes = likeRepository.findByUserUserIdAndMovieIsNotNull(userId, pageable)
        val movies = likes.content.mapNotNull { it.movie }.sortedByDescending { it.updatedAt }

        return PageImpl(movies, pageable, likes.totalElements)
    }

    fun getLikedTVShowsByUserId(userId: String, page: Int, size: Int): Page<TVShowEntity> {
        val pageable = PageRequest.of(page, size)
        val likes = likeRepository.findByUserUserIdAndTvShowIsNotNull(userId, pageable)
        val tvShows = likes.content.mapNotNull { it.tvShow }.sortedByDescending { it.updatedAt }

        return PageImpl(tvShows, pageable, likes.totalElements)
    }

    @Transactional
    fun likeContent(userId: String, addLikeRequest: LikeDto.LikeRequest): Like? {
        val user = userRepository.findByUserId(userId) ?: throw UserNotFoundException("해당 유저를 찾을 수 없습니다.")

        addLikeRequest.movieId?.let {
            return likeMovie(user, it)
        }

        addLikeRequest.tvShowId?.let {
            return likeTVShow(user, it)
        }

        return null
    }

    private fun likeMovie(user: User, movieId: Long): Like {
        val movie = movieRepository.findById(movieId.toInt())
            .orElseThrow { MovieNotFoundException("해당 영화를 찾을 수 없습니다.") }
        return likeRepository.save(Like(user = user, movie = movie, null))
    }

    private fun likeTVShow(user: User, tvShowId: Long): Like {
        val tvShow = tvShowRepository.findById(tvShowId.toInt())
            .orElseThrow { TVShowNotFoundException("해당 TV 프로그램을 찾을 수 없습니다.") }
        return likeRepository.save(Like(user = user, null, tvShow = tvShow))
    }

    fun cancelLike(userId: String, likeRequest: LikeDto.LikeRequest) {
        likeRequest.movieId?.let {
            val like = likeRepository.findByUserUserIdAndMovieId(userId, it) ?: throw LikeNotFoundException(
                "해당 영화에 대한 좋아요를 찾을 수 없습니다."
            )
            likeRepository.delete(like)
        }

        likeRequest.tvShowId?.let {
            val like = likeRepository.findByUserUserIdAndTvShowId(userId, it) ?: throw LikeNotFoundException(
                "해당 TV 프로그램에 대한 좋아요를 찾을 수 없습니다."
            )
            likeRepository.delete(like)
        }
    }
}
