package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.GenreEntity

data class MovieDetailsDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val genres: List<GenreEntity>,
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
    val directorName: String?, // 감독 이름 필드 추가
    val directorProfilePath: String? // 감독 프로필 경로 필드 추가
) {
    val fullBackdropPath: String
        get() = "https://image.tmdb.org/t/p/w500$backdrop_path"

    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/w500$poster_path"
}
