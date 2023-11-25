package elice.team5th.domain.clicks.dto

data class TVShowWithClicksDto(
    val tvShowId: Long,
    val name: String,
    val posterPath: String,
    val clicks: Int
)
