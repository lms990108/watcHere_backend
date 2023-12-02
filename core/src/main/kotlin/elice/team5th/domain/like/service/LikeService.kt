package elice.team5th.domain.like.service

import elice.team5th.domain.like.dto.LikeDto
import elice.team5th.domain.like.exception.AlreadyLikedContentException
import elice.team5th.domain.like.exception.LikeNotFoundException
import elice.team5th.domain.like.model.Like
import elice.team5th.domain.like.repository.LikeRepository
import elice.team5th.domain.tmdb.entity.MovieEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import elice.team5th.domain.tmdb.enumtype.ContentType
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
    fun likeContent(userId: String, contentType: ContentType, contentId: Int): Like? {
        val user = userRepository.findByUserId(userId) ?: throw UserNotFoundException("해당 유저를 찾을 수 없습니다.")
        return when (contentType) {
            ContentType.MOVIE -> {
                val movie = movieRepository.findById(contentId)
                    .orElseThrow { MovieNotFoundException("해당 영화를 찾을 수 없습니다.") }

                likeRepository.findByUserAndMovie(user, movie)?.let {
                    throw AlreadyLikedContentException("이미 좋아요를 누른 영화입니다.")
                }

                likeRepository.save(Like(user = user, movie = movie))
            }

            ContentType.TV -> {
                val tvShow = tvShowRepository.findById(contentId)
                    .orElseThrow { TVShowNotFoundException("해당 TV 프로그램을 찾을 수 없습니다.") }

                likeRepository.findByUserAndTvShow(user, tvShow)?.let {
                    throw AlreadyLikedContentException("이미 좋아요를 누른 TV 프로그램입니다.")
                }

                likeRepository.save(Like(user = user, tvShow = tvShow))
            }
        }
    }

    @Transactional
    fun cancelLike(userId: String, contentType: ContentType, contentId: Int) {
        when (contentType) {
            ContentType.MOVIE -> {
                val movie = movieRepository.findById(contentId)
                    .orElseThrow { MovieNotFoundException("해당 영화를 찾을 수 없습니다.") }
                likeRepository.deleteByUserUserIdAndMovie(userId, movie)
            }
            ContentType.TV -> {
                val tvShow = tvShowRepository.findById(contentId)
                    .orElseThrow { TVShowNotFoundException("해당 TV 프로그램을 찾을 수 없습니다.") }
                likeRepository.deleteByUserUserIdAndTvShow(userId, tvShow)
            }
        }
    }

    fun isLikedContent(userId: String, contentType: ContentType, contentId: Int): Boolean {
        return when (contentType) {
            ContentType.MOVIE -> {
                val movie = movieRepository.findById(contentId)
                    .orElseThrow { MovieNotFoundException("해당 영화를 찾을 수 없습니다.") }
                likeRepository.existsByUserUserIdAndMovie(userId, movie)
            }
            ContentType.TV -> {
                val tvShow = tvShowRepository.findById(contentId)
                    .orElseThrow { TVShowNotFoundException("해당 TV 프로그램을 찾을 수 없습니다.") }
                likeRepository.existsByUserUserIdAndTvShow(userId, tvShow)
            }
        }
    }
}
