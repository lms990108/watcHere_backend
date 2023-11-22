package elice.team5th.domain.review.dto

data class CreateReviewDTO(
    val contentId: Long,
    val detail: String,
    val rating: Double
)
