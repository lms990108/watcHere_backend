package elice.team5th.domain.review.dto

data class ReviewDTO(
    val id: Long? = null,
    val userId: Long,
    val contentId: Long,
    val detail: String,
    val rating: Int,
    val likes: Int,
    val reports: Int
)
