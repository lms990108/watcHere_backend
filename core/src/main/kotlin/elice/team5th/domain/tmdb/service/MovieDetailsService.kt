package elice.team5th.domain.tmdb.service

import elice.team5th.domain.clicks.service.ContentClickService
import elice.team5th.domain.tmdb.dto.ActorDto
import elice.team5th.domain.tmdb.dto.MovieApiResponseDto
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
    private val movieRepository: MovieRepository, // 레포지토리 인터페이스 주입
    private val contentClickService: ContentClickService
) {
    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getMovieDetails(movieId: Int): Mono<MovieDetailsDto> {
        println("Attempting to retrieve movie details for movie ID: $movieId")

        val movieEntity = movieRepository.findById(movieId)
        if (movieEntity.isPresent) {
            contentClickService.incrementMovieClicks(movieId.toLong()) // 조회수 증가 로직 추가
            println("Movie details found in the database for movie ID: $movieId")
            return Mono.just(convertToDto(movieEntity.get()))
        } else {
            println("Movie details not found in the database for movie ID: $movieId. Making API call.")
        }

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/movie/$movieId")
                    .queryParam("append_to_response", "videos,credits")
                    .queryParam("language", "ko-KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(MovieApiResponseDto::class.java)
            .doOnNext { response ->
                println("Received response from TMDB API for movie ID: $movieId")
            }
            .map(this::convertApiResponseToDto)
            .doOnError { error ->
                println("Error occurred while retrieving movie details for movie ID: $movieId - Error: $error")
            }
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
                    profilePath = actorEntity.profilePath
                        ?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
                )
            },
            videos = movieEntity.videos.map { videoEntity ->
                VideoDto(
                    key = videoEntity.key,
                    name = videoEntity.name,
                    site = videoEntity.site,
                    type = videoEntity.type
                )
            },
            directorName = movieEntity.directorName, // 감독 이름 매핑
            directorProfilePath = movieEntity.directorProfilePath // 감독 프로필 경로 매핑
        )
    }

    private fun convertApiResponseToDto(apiResponse: MovieApiResponseDto): MovieDetailsDto {
        return MovieDetailsDto(
            adult = apiResponse.adult,
            backdrop_path = apiResponse.backdrop_path,
            genres = apiResponse.genres,
            id = apiResponse.id,
            original_language = apiResponse.original_language,
            original_title = apiResponse.original_title,
            overview = apiResponse.overview,
            popularity = apiResponse.popularity,
            poster_path = apiResponse.poster_path,
            release_date = apiResponse.release_date,
            runtime = apiResponse.runtime,
            title = apiResponse.title,
            video = apiResponse.video,
            vote_average = apiResponse.vote_average,
            vote_count = apiResponse.vote_count,
            actors = apiResponse.credits.cast,
            videos = apiResponse.videos.results,
            directorName = apiResponse.credits.crew.firstOrNull { it.job == "Director" }?.name ?: "Unknown",
            directorProfilePath = apiResponse.credits.crew
                .firstOrNull { it.job == "Director" }?.profile_path
                ?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
        )
    }
}
