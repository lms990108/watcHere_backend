package elice.team5th.domain.clicks.dto

data class MovieWithClicksDto(
    val movieId: Long,
    val title: String,
    val posterPath: String,
    var clicks: Int
)
