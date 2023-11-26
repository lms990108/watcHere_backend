package elice.team5th.domain.tmdb.service

import elice.team5th.domain.tmdb.dto.ActorDto
import elice.team5th.domain.tmdb.dto.MovieDetailsDto
import elice.team5th.domain.tmdb.dto.VideoDto
import elice.team5th.domain.tmdb.entity.GenreEntity
import elice.team5th.domain.tmdb.entity.MovieEntity
import elice.team5th.domain.tmdb.repository.MovieRepository
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class MovieDetailsService(
    private val webClient: WebClient,
    private val movieRepository: MovieRepository // 레포지토리 인터페이스 주입
) {
    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getMovieDetails(movieId: Int): Mono<MovieDetailsDto> {
        // 먼저 데이터베이스에서 영화 상세 정보를 조회
        val movieEntity = movieRepository.findById(movieId)
        if (movieEntity.isPresent) {
            // MovieEntity를 MovieDetailsDto로 변환하고 반환
            println("Found movie details in the database for movie ID: $movieId")
            return Mono.just(convertToDto(movieEntity.get()))
        }

        // DB에 없다면 API 호출
        println("Movie details not found in the database for movie ID: $movieId. Making API call.")
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/movie/$movieId")
                    .queryParam("language", "ko-KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(MovieDetailsDto::class.java)
            .onErrorMap(ErrorUtil::handleCommonErrors)
    }

    private fun fetchMovieDetailsFromApi(movieId: Int): Mono<MovieDetailsDto> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/movie/$movieId")
                    .queryParam("language", "ko-KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(MovieDetailsDto::class.java)
            .onErrorMap(ErrorUtil::handleCommonErrors)
    }

    private fun convertToDto(movieEntity: MovieEntity): MovieDetailsDto {
        return MovieDetailsDto(
            adult = movieEntity.adult,
            backdrop_path = movieEntity.backdropPath,
            genres = movieEntity.genres.map { genreEntity ->
                GenreEntity(id = genreEntity.id, name = genreEntity.name)
            },
            id = movieEntity.id.toInt(),
            original_language = movieEntity.originalLanguage,
            original_title = movieEntity.originalTitle,
            overview = movieEntity.overview,
            popularity = movieEntity.popularity,
            poster_path = movieEntity.posterPath,
            release_date = movieEntity.releaseDate.toString(), // Date 형식을 String으로 변환
            runtime = movieEntity.runtime,
            title = movieEntity.title,
            video = movieEntity.video,
            vote_average = movieEntity.voteAverage,
            vote_count = movieEntity.voteCount,
            actors = movieEntity.actors.map { actorEntity ->
                ActorDto(
                    id = actorEntity.id ?: throw IllegalStateException("Actor ID cannot be null"),
                    name = actorEntity.name ?: "Unknown",
                    profilePath = actorEntity.profilePath ?: ""
                )
            },
            videos = movieEntity.videos.map { videoEntity ->
                VideoDto(
                    id = videoEntity.id ?: throw IllegalStateException("Video ID cannot be null"),
                    key = videoEntity.key,
                    name = videoEntity.name,
                    site = videoEntity.site,
                    type = videoEntity.type
                )
            }

        )
    }
}
