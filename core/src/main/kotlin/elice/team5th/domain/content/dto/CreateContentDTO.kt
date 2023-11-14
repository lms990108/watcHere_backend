package elice.team5th.domain.content.dto

data class CreateContentDTO(
    val title: String,
    val type: String,
    val posterImage: String?,
    val starRating: Float,
    val director: String?
)
