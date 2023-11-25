package elice.team5th.domain.clicks.service

import elice.team5th.domain.clicks.dto.MovieWithClicksDto
import elice.team5th.domain.clicks.dto.TVShowWithClicksDto
import elice.team5th.domain.clicks.entity.MovieClicksEntity
import elice.team5th.domain.clicks.entity.TVShowClicksEntity
import elice.team5th.domain.clicks.repository.MovieClicksRepository
import elice.team5th.domain.clicks.repository.TVShowClicksRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContentClickService(
    private val movieClicksRepository: MovieClicksRepository,
    private val tvShowClicksRepository: TVShowClicksRepository
) {
    // 영화 조회수 증가
    @Transactional
    fun incrementMovieClicks(movieId: Long) {
        val movieClicks = movieClicksRepository.findById(movieId).orElseGet {
            movieClicksRepository.save(MovieClicksEntity(movieId, 0))
        }
        movieClicks.clicks++
        movieClicksRepository.save(movieClicks)
    }

    // TV 쇼 조회수 증가
    @Transactional
    fun incrementTVShowClicks(tvShowId: Long) {
        val tvShowClicks = tvShowClicksRepository.findById(tvShowId).orElseGet {
            tvShowClicksRepository.save(TVShowClicksEntity(tvShowId, 0))
        }
        tvShowClicks.clicks++
        tvShowClicksRepository.save(tvShowClicks)
    }

    // 조회수가 높은 영화 목록 가져오기 (페이징 처리)
    fun getTopMoviesByClicks(page: Int, size: Int): Page<MovieWithClicksDto> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "clicks"))
        return movieClicksRepository.findAllMoviesWithClicks(pageable)
    }

    // 조회수가 높은 TV 쇼 목록 가져오기 (페이징 처리)
    fun getTopTVShowsByClicks(page: Int, size: Int = 10): Page<TVShowWithClicksDto> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "clicks"))
        return tvShowClicksRepository.findAllTVShowsWithClicks(pageable)
    }
}
