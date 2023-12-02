package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.GenreEntity

data class TVDetailsDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val first_air_date: String,
    val genres: List<GenreEntity>,
    val id: Int,
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
    val directorName: String?, // 감독 이름 필드 추가
    val directorProfilePath: String? // 감독 프로필 경로 필드 추가
) {
    val fullBackdropPath: String
        get() = backdrop_path.prependBaseUrl()

    val fullPosterPath: String
        get() = poster_path.prependBaseUrl()
}

private fun String?.prependBaseUrl(baseUrl: String = "https://image.tmdb.org/t/p/w500"): String {
    return this?.let { baseUrl + it } ?: ""
}
