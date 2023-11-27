package elice.team5th.domain.tmdb.service

import elice.team5th.domain.clicks.service.ContentClickService
import elice.team5th.domain.tmdb.dto.*
import elice.team5th.domain.tmdb.entity.GenreEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import elice.team5th.domain.tmdb.repository.TVShowRepository
import elice.team5th.domain.tmdb.util.ErrorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*

@Service
class TVDetailsService(
    private val webClient: WebClient,
    private val tvShowRepository: TVShowRepository,
    private val contentClickService: ContentClickService
) {

    @Value("\${tmdb.api.access-token}")
    private lateinit var accessToken: String

    fun getTVDetails(tvId: Int): Mono<TVDetailsDto> {
        println("Fetching TV show details for ID: $tvId") // 로그
        val tvShowEntity = tvShowRepository.findById(tvId)
        if (tvShowEntity.isPresent) {
            contentClickService.incrementTVShowClicks(tvId.toLong()) // 조회수 증가 로직 추가
            println("Found TV show details in the database for ID: $tvId") // 로그
            return Mono.just(convertEntityToDto(tvShowEntity.get()))
        }

        println("TV show details not found in the database for ID: $tvId. Fetching from TMDB API.") // 로그
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/tv/$tvId")
                    .queryParam("append_to_response", "videos,credits")
                    .queryParam("language", "ko-KR")
                    .build()
            }
            .header("Authorization", accessToken)
            .header("accept", "application/json")
            .retrieve()
            .bodyToMono(TVShowApiResponseDto::class.java)
            .doOnSuccess { println("Received API response for TV show ID: $tvId") }
            .doOnError { error -> println("Error during API call for TV show ID: $tvId: $error") }
            .map(this::convertApiResponseToDto)
            .onErrorMap(ErrorUtil::handleCommonErrors)
    }

    private fun convertEntityToDto(entity: TVShowEntity): TVDetailsDto {
        return TVDetailsDto(
            adult = entity.adult,
            backdrop_path = entity.backdropPath,
            genres = entity.genres.map { genreEntity ->
                GenreEntity(id = genreEntity.id, name = genreEntity.name)
            },
            id = entity.id.toInt(),
            name = entity.name,
            number_of_episodes = entity.numberOfEpisodes,
            number_of_seasons = entity.numberOfSeasons,
            overview = entity.overview,
            popularity = entity.popularity,
            poster_path = entity.posterPath,
            first_air_date = entity.firstAirDate.toString(),
            last_air_date = entity.lastAirDate.toString(),
            type = entity.type,
            vote_average = entity.voteAverage,
            vote_count = entity.voteCount,
            videos = entity.videos.map {
                VideoDto(
                    key = it.key,
                    name = it.name,
                    site = it.site,
                    type = it.type
                )
            },
            actors = entity.actors.map {
                ActorDto(
                    id = it.id ?: throw IllegalStateException("Actor ID cannot be null"),
                    name = it.name ?: "Unknown",
                    profilePath = it.profilePath?.let { path -> "https://image.tmdb.org/t/p/w500$path" } ?: ""
                )
            },
            directorName = entity.directorName, // 감독 이름 매핑
            directorProfilePath = entity.directorProfilePath // 감독 프로필 경로 매핑
        )
    }


    private fun convertApiResponseToDto(apiResponse: TVShowApiResponseDto): TVDetailsDto {
        val director = apiResponse.created_by.firstOrNull()
        return TVDetailsDto(
            adult = apiResponse.adult,
            backdrop_path = apiResponse.backdrop_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
            first_air_date = apiResponse.first_air_date,
            genres = apiResponse.genres.map { GenreEntity(it.id, it.name) },
            id = apiResponse.id,
            last_air_date = apiResponse.last_air_date,
            name = apiResponse.name,
            number_of_episodes = apiResponse.number_of_episodes,
            number_of_seasons = apiResponse.number_of_seasons,
            overview = apiResponse.overview,
            popularity = apiResponse.popularity,
            poster_path = apiResponse.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
            type = apiResponse.type,
            vote_average = apiResponse.vote_average,
            vote_count = apiResponse.vote_count,
            videos = apiResponse.videos.results,
            actors = apiResponse.credits.cast,
            directorName = director?.name, // 감독 이름 매핑
            directorProfilePath = director?.profile_path?.let { "https://image.tmdb.org/t/p/w500$it" } // 감독 프로필 경로 매핑
        )
    }


}
