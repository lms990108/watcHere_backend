package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.GenreEntity

data class MovieApiResponseDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val genres: List<GenreEntity>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val runtime: Int,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val videos: VideoResponse,
    val credits: CreditsResponse
)

data class CreditsResponse(
    val cast: List<ActorDto>,
    val crew: List<CrewMember>
)

data class CrewMember(
    val name: String,
    val profile_path: String?,
    val job: String
)
