package elice.team5th.domain.tmdb.dto

data class MovieDetailsDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val genres: List<Genre>,
    val id: Int,
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
    val vote_count: Int
){
    val fullBackdropPath: String
        get() = "https://image.tmdb.org/t/p/w500${backdrop_path}"

    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/w500${poster_path}"
}

data class Genre(
    val id: Int,
    val name: String
)

data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)
