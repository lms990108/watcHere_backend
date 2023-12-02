package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.GenreEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import java.time.LocalDateTime

class TVShowDto {
    data class Response(
        val adult: Boolean,
        val backdrop_path: String?,
        val first_air_date: String,
        val genres: List<GenreDto>,
        val id: Long,
        val last_air_date: String,
        val name: String,
        val number_of_episodes: Int,
        val number_of_seasons: Int,
        val overview: String,
        val popularity: Double,
        val poster_path: String?,
        val type: String,
        val vote_average: Double,
        val vote_count: Int,
        val videos: List<VideoDto>,
        val actors: List<ActorDto>,
        val directorName: String?,
        val directorProfilePath: String?,
        val updatedAt: LocalDateTime?,
        val createdAt: LocalDateTime?
    ) {
        constructor(tvShow: TVShowEntity) : this(
            id = tvShow.id,
            adult = tvShow.adult,
            backdrop_path = tvShow.backdropPath,
            genres = tvShow.genres.map { GenreDto(it) },
            first_air_date = tvShow.firstAirDate.toString(),
            last_air_date = tvShow.lastAirDate.toString(),
            name = tvShow.name,
            number_of_episodes = tvShow.numberOfEpisodes,
            number_of_seasons = tvShow.numberOfSeasons,
            overview = tvShow.overview,
            popularity = tvShow.popularity,
            poster_path = tvShow.posterPath,
            type = tvShow.type,
            vote_average = tvShow.voteAverage,
            vote_count = tvShow.voteCount,
            videos = tvShow.videos.map { VideoDto(it) },
            actors = tvShow.actors.map { ActorDto(it) },
            directorName = tvShow.directorName,
            directorProfilePath = tvShow.directorProfilePath,
            updatedAt = tvShow.updatedAt,
            createdAt = tvShow.createdAt
        )
    }
}
