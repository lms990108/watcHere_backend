package elice.team5th.domain.content.dto

data class ContentDetailDto(
    val id: Long? = null,
    val type: Int,
    val title: String,
    val posterImageUrl: String,
    val starRating: Float,
    val director: String,
    val provider: String
)
