package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.GenreEntity
import elice.team5th.domain.tmdb.entity.MovieEntity
import java.time.LocalDateTime

class MovieDto {
    data class Response(
        val adult: Boolean,
        val backdrop_path: String?,
        val genres: List<GenreDto>,
        val id: Long,
        val original_language: String,
        val original_title: String,
        val overview: String,
        val popularity: Double,
        val poster_path: String?,
        val release_date: String,
        val runtime: Int?,
        val title: String,
        val video: Boolean,
        val vote_average: Double,
        val vote_count: Int,
        val actors: List<ActorDto>,
        val videos: List<VideoDto>,
        val directorName: String?,
        val directorProfilePath: String?,
        val updatedAt: LocalDateTime?,
        val createdAt: LocalDateTime?
    ) {
        constructor(movie: MovieEntity) : this(
            id = movie.id,
            adult = movie.adult,
            backdrop_path = movie.backdropPath,
            genres = movie.genres.map { GenreDto(it) },
            original_language = movie.originalLanguage,
            original_title = movie.originalTitle,
            overview = movie.overview,
            popularity = movie.popularity,
            poster_path = movie.posterPath,
            release_date = movie.releaseDate.toString(),
            runtime = movie.runtime,
            title = movie.title,
            video = movie.video,
            vote_average = movie.voteAverage,
            vote_count = movie.voteCount,
            actors = movie.actors.map { ActorDto(it) },
            videos = movie.videos.map { VideoDto(it) },
            directorName = movie.directorName,
            directorProfilePath = movie.directorProfilePath,
            updatedAt = movie.updatedAt,
            createdAt = movie.createdAt
        )
    }
}
