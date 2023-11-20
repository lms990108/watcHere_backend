package elice.team5th.domain.tmdb.dto

data class TVDetailsDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val created_by: List<Any>,
    val episode_run_time: List<Int>,
    val first_air_date: String,
    val genres: List<Genre>,
    val id: Int,
    val last_air_date: String,
    val last_episode_to_air: Episode,
    val name: String,
    val next_episode_to_air: Episode?,
    val networks: List<Network>,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val seasons: List<Season>,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
){
    val fullBackdropPath: String
        get() = backdrop_path.prependBaseUrl()

    val fullPosterPath: String
        get() = poster_path.prependBaseUrl()
}

data class Episode(
    val id: Int,
    val name: String,
    val overview: String,
    val vote_average: Double,
    val vote_count: Int,
    val air_date: String,
    val episode_number: Int,
    val episode_type: String,
    val production_code: String,
    val runtime: Int,
    val season_number: Int,
    val show_id: Int,
    val still_path: String?
){
    val fullStillPath: String
        get() = still_path.prependBaseUrl()
}

data class Network(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
){
    val fullLogoPath: String
        get() = logo_path.prependBaseUrl()
}

data class Season(
    val air_date: String,
    val episode_count: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val season_number: Int,
    val vote_average: Double
){
    val fullPosterPath: String
        get() = poster_path.prependBaseUrl()
}

private fun String?.prependBaseUrl(baseUrl: String = "https://image.tmdb.org/t/p/w500"): String {
    return this?.let { baseUrl + it } ?: ""
}
