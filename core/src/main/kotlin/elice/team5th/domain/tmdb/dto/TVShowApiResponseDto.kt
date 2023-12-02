package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.GenreEntity

data class TVShowApiResponseDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val created_by: List<DirectorDto>,
    val genres: List<GenreEntity>,
    val id: Int,
    val name: String,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val first_air_date: String?,
    val last_air_date: String?,
    val type: String,
    val vote_average: Double,
    val vote_count: Int,
    val videos: VideoResponse,
    val credits: CreditsResponse
)

data class DirectorDto(
    val id: Int,
    val name: String,
    val profile_path: String?
)
