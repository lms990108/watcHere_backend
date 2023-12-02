package elice.team5th.domain.clicks.service

import elice.team5th.common.service.RedisService
import elice.team5th.domain.clicks.dto.MovieWithClicksDto
import elice.team5th.domain.clicks.dto.TVShowWithClicksDto
import elice.team5th.domain.clicks.entity.MovieClicksEntity
import elice.team5th.domain.clicks.entity.TVShowClicksEntity
import elice.team5th.domain.clicks.repository.MovieClicksRepository
import elice.team5th.domain.clicks.repository.TVShowClicksRepository
import elice.team5th.domain.tmdb.exception.MovieNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContentClickService(
    private val movieClicksRepository: MovieClicksRepository,
    private val tvShowClicksRepository: TVShowClicksRepository,
    private val redisService: RedisService
) {
    private val movieClicksKeyPrefix = "movieClicks:"
    private val tvShowClicksKeyPrefix = "tvShowClicks:"

    // 영화 조회수 증가
    @Transactional
    fun incrementMovieClicks(movieId: Long) {
        val redisKey = "$movieClicksKeyPrefix$movieId"
        val currentClicks = redisService.getValue(redisKey)?.toInt()
            ?: movieClicksRepository.findById(movieId).map { it.clicks }.orElse(0)

        redisService.setValue(redisKey, (currentClicks + 1).toString())
    }

    // TV 쇼 조회수 증가
    @Transactional
    fun incrementTVShowClicks(tvShowId: Long) {
        val redisKey = "$tvShowClicksKeyPrefix$tvShowId"
        val currentClicks = redisService.getValue(redisKey)?.toInt()
            ?: tvShowClicksRepository.findById(tvShowId).map { it.clicks }.orElse(0)

        redisService.setValue(redisKey, (currentClicks + 1).toString())
    }

    // 조회수가 높은 영화 목록 가져오기 (페이징 처리)
    fun getTopMoviesByClicks(page: Int, size: Int): Page<MovieWithClicksDto> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "clicks"))
        val moviesWithClicks =  movieClicksRepository.findAllMoviesWithClicks(pageable)

        moviesWithClicks.content.forEach { movieWithClicks ->
            val movieId = movieWithClicks.movieId
            val redisKey = "$movieClicksKeyPrefix$movieId"
            movieWithClicks.clicks = redisService.getValue(redisKey)?.toInt() ?: movieWithClicks.clicks
        }

        return moviesWithClicks
    }

    // 조회수가 높은 TV 쇼 목록 가져오기 (페이징 처리)
    fun getTopTVShowsByClicks(page: Int, size: Int = 10): Page<TVShowWithClicksDto> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "clicks"))
        val tvShowWithClicks = tvShowClicksRepository.findAllTVShowsWithClicks(pageable)

        tvShowWithClicks.content.forEach { tvShowWithClicks ->
            val tvShowId = tvShowWithClicks.tvShowId
            val redisKey = "$tvShowClicksKeyPrefix$tvShowId"
            tvShowWithClicks.clicks = redisService.getValue(redisKey)?.toInt() ?: tvShowWithClicks.clicks
        }

        return tvShowWithClicks
    }

    // 배치로 빼는 게 더 좋을 듯
    @Scheduled(fixedRate = 7200000) // 2시간마다 실행
    fun syncClicksToDb() {
        val movieClicksKeys = redisService.getKeys("$movieClicksKeyPrefix*")
        val tvShowClicksKeys = redisService.getKeys("$tvShowClicksKeyPrefix*")

        movieClicksKeys?.forEach { key ->
            val movieId = key.substringAfter(movieClicksKeyPrefix).toLong()
            val clicks = redisService.getValue(key)?.toInt() ?: 0
            val movieClicksEntity = MovieClicksEntity(movieId, clicks)
            movieClicksRepository.save(movieClicksEntity)
            redisService.deleteValue(key)
        }

        tvShowClicksKeys?.forEach { key ->
            val tvShowId = key.substringAfter(tvShowClicksKeyPrefix).toLong()
            val clicks = redisService.getValue(key)?.toInt() ?: 0
            val tvShowClicksEntity = TVShowClicksEntity(tvShowId, clicks)
            tvShowClicksRepository.save(tvShowClicksEntity)
            redisService.deleteValue(key)
        }
    }
}
